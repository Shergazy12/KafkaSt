package com.zstan.kafka.st.Telegram;


import com.zstan.kafka.st.Entity.Category;
import com.zstan.kafka.st.Entity.MenuItem;
import com.zstan.kafka.st.Entity.Product;
import com.zstan.kafka.st.Entity.Restaurant;
import com.zstan.kafka.st.OpenAI.OpenAIService;
import com.zstan.kafka.st.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Value("${payment.currency}")
    private String paymentCurrency;

    // Метод возвращает имя бота
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    // Метод возвращает токен бота
    @Override
    public String getBotToken() {
        return botToken;
    }

    // Метод обрабатывает входящие обновления от Telegram
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String chatId = update.getMessage().getChatId().toString();
                String userMessage = update.getMessage().getText();

                String botResponse = handleCommand(userMessage, Long.parseLong(chatId));

                if (botResponse != null) {
                    sendMessage(chatId, botResponse);
                }
            } else if (update.hasCallbackQuery()) {
                String callbackData = update.getCallbackQuery().getData();
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

                String botResponse = handleCallback(callbackData, Long.parseLong(chatId));
                if (botResponse != null) {
                    editMessage(chatId, update.getCallbackQuery().getMessage().getMessageId(), botResponse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод обрабатывает текстовые команды от пользователя
    private String handleCommand(String command, Long userId) {
        if (command.equalsIgnoreCase("/start")) {
            return "👋 Добро пожаловать в SHAKH_Bot!\n" +
                    "Доступные команды:\n" +
                    "/categories - посмотреть категории\n" +
                    "/restaurants - список ресторанов\n" +
                    "/cart - проверить корзину\n" +
                    "/pay - оплатить заказ\n" +
                    "/help - справка\n";
        } else if (command.equalsIgnoreCase("/categories")) {
            return sendCategories(userId);
        } else if (command.equalsIgnoreCase("/restaurants")) {
            sendRestaurants(userId);
            return null;
        } else if (command.equalsIgnoreCase("/cart")) {
            return cartService.getCartContent(userId);
        } else if (command.equalsIgnoreCase("/pay")) {
            return processPayment(userId);
        } else if (command.equalsIgnoreCase("/help")) {
            return "🛠️ Доступные команды:\n" +
                    "/categories - посмотреть категории\n" +
                    "/restaurants - список ресторанов\n" +
                    "/cart - проверить корзину\n" +
                    "/pay - оплатить заказ\n" +
                    "/help - помощь по командам\n" +
                    "Вы также можете задать любой вопрос.";
        } else {
            return handleOpenAIResponse(command);
        }
    }

    // Обработка произвольного текста через OpenAI
    private String handleOpenAIResponse(String command) {
        try {
            String aiResponse = openAIService.getGPTResponse(command);
            return aiResponse != null ? aiResponse : "⚠️ Ошибка при обработке вашего запроса.";
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Ошибка: не удалось получить ответ от AI.";
        }
    }

    // Обработка действий с кнопками (callback-данные)
    private String handleCallback(String callbackData, Long userId) {
        if (callbackData.startsWith("/menu")) {
            Long restaurantId = Long.parseLong(callbackData.split(" ")[1]);
            sendMenu(restaurantId, userId);
            return null;
        } else if (callbackData.startsWith("/add_to_cart")) {
            String[] parts = callbackData.split(" ");
            Long productId = Long.parseLong(parts[1]);
            int quantity = Integer.parseInt(parts[2]);
            return cartService.addToCart(userId, productId, quantity);
        } else if (callbackData.startsWith("/products")) {
            Long categoryId = Long.parseLong(callbackData.split(" ")[1]);
            sendProducts(categoryId, userId);
            return null;
        }
        return "⚠️ Действие не распознано.";
    }

    // Метод отправляет список продуктов из выбранной категории
    private void sendProducts(Long categoryId, Long userId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        if (products.isEmpty()) {
            sendMessage(userId.toString(), "⚠️ В этой категории пока нет товаров.");
            return;
        }

        for (Product product : products) {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(userId.toString());

            // Проверяем, есть ли URL изображения
            String imageUrl = product.getImageUrl();
            if (imageUrl == null || imageUrl.isEmpty()) {
                // Если URL изображения пуст, устанавливаем placeholder
                imageUrl = "https://via.placeholder.com/300?text=Нет+изображения";
            }
            photo.setPhoto(new InputFile(imageUrl));

            String caption = "🛍️ " + product.getName() + "\n" +
                    "💵 Цена: " + product.getPrice() + " руб.\n" +
                    "📄 " + product.getDescription();
            photo.setCaption(caption);

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(InlineKeyboardButton.builder()
                    .text("🛒 Добавить в корзину")
                    .callbackData("/add_to_cart " + product.getId() + " 1")
                    .build());
            rows.add(row);
            keyboard.setKeyboard(rows);

            photo.setReplyMarkup(keyboard);

            try {
                execute(photo);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод отправляет список ресторанов пользователю
    private void sendRestaurants(Long chatId) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        if (restaurants.isEmpty()) {
            sendMessage(chatId.toString(), "⚠️ Рестораны пока недоступны.");
            return;
        }

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(InlineKeyboardButton.builder()
                    .text(restaurant.getName() + (restaurant.isOpen() ? " 🟢" : " 🔴"))
                    .callbackData("/menu " + restaurant.getId())
                    .build());
            rows.add(row);
        }

        keyboard.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("🏠 Список ресторанов:");
        message.setReplyMarkup(keyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Метод отправляет меню ресторана пользователю
    private void sendMenu(Long restaurantId, Long chatId) {
        List<MenuItem> menu = menuService.getMenuByRestaurantId(restaurantId);
        if (menu.isEmpty()) {
            sendMessage(chatId.toString(), "⚠️ В меню этого ресторана пока нет товаров.");
            return;
        }

        for (MenuItem item : menu) {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(chatId.toString());
            photo.setPhoto(new InputFile(item.getImageUrl()));

            String caption = "🍴 " + item.getName() + "\n" +
                    "💵 Цена: " + item.getPrice() + " сом.\n" +
                    "📄 " + item.getDescription();
            photo.setCaption(caption);

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(InlineKeyboardButton.builder()
                    .text("🛒 Добавить в корзину")
                    .callbackData("/add_to_cart " + item.getId() + " 1")
                    .build());
            rows.add(row);
            keyboard.setKeyboard(rows);

            photo.setReplyMarkup(keyboard);

            try {
                execute(photo);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод обрабатывает оплату корзины
    private String processPayment(Long userId) {
        try {
            Long amount = (long) cartService.getCartTotal(userId);
            if (amount == 0) {
                return "🛒 Ваша корзина пуста. Добавьте товары для оформления оплаты.";
            }

            String paymentLink = paymentService.createPaymentIntent(amount * 100, paymentCurrency);

            if (paymentLink != null) {
                return "💳 Для завершения оплаты перейдите по ссылке:\n" + paymentLink;
            } else {
                return "⚠️ Не удалось создать оплату. Попробуйте позже.";
            }
        } catch (Exception e) {
            return "⚠️ Не удалось создать оплату. Попробуйте позже.";
        }
    }

    // Метод отправляет категории продуктов пользователю
    private String sendCategories(Long userId) {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return "⚠️ Категорий пока нет.";
        }

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Category category : categories) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(InlineKeyboardButton.builder()
                    .text(category.getName())
                    .callbackData("/products " + category.getId())
                    .build());
            rows.add(row);
        }

        keyboard.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(userId.toString());
        message.setText("Выберите категорию:");
        message.setReplyMarkup(keyboard);

        try {
            execute(message);
            return null;
        } catch (TelegramApiException e) {
            return "⚠️ Ошибка при загрузке категорий. Попробуйте позже.";
        }
    }

    // Метод отправляет текстовое сообщение пользователю
    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Метод редактирует сообщение пользователя
    private void editMessage(String chatId, Integer messageId, String text) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}


