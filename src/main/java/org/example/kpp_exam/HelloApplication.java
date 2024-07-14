package org.example.kpp_exam;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;


public class HelloApplication extends Application {
    private static BlaBlaCar blaBlaCar;
    private final int height=600;
    private final int width=500;
    @Override
    public void start(Stage stage) {

        stage.setTitle("BlaBlaCar Yefimova");
        VBox mainLayout=new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel=new Label("Вітаємо у BlaBlaCar");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button registerButton=new Button("Увійти");
        registerButton.setOnAction(actionEvent -> showRegisterForm(stage));

        mainLayout.getChildren().addAll(welcomeLabel,registerButton);

        Scene scene=new Scene(mainLayout,width,height);
        stage.setScene(scene);
        stage.show();
    }

    private void showRegisterForm(Stage stage) {
        VBox layout=new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label registerLabel = new Label("Увійти як пасажир або водій?");
        registerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ToggleGroup userTypeGroup = new ToggleGroup();
        RadioButton passengerRadio = new RadioButton("Пасажир");
        passengerRadio.setToggleGroup(userTypeGroup);
        RadioButton driverRadio = new RadioButton("Водій");
        driverRadio.setToggleGroup(userTypeGroup);

        TextField nameField = new TextField();
        nameField.setPromptText("Введіть своє ім'я");

        Button submitButton = new Button("Далі");
        submitButton.setOnAction(e -> {
            if (passengerRadio.isSelected()) {
                Passenger foundPassenger = null;
                for (Passenger passenger : blaBlaCar.getPassengerList()) {
                    if (passenger.getName().equals(nameField.getText())) {
                        foundPassenger = passenger;
                        break;
                    }
                }
                if (foundPassenger != null) {
                    showPassengerInterface(stage, foundPassenger);
                    System.out.println("Existing Passenger logged in, id: " + foundPassenger.getId() + ", name: " + foundPassenger.getName());
                } else {
                    Passenger newPassenger = new Passenger(blaBlaCar.getPassengerList().size() + 1, nameField.getText());
                    blaBlaCar.registerPassenger(newPassenger);
                    showPassengerInterface(stage, newPassenger);
                    System.out.println("New Passenger registered, id: " + newPassenger.getId() + ", name: " + newPassenger.getName());
                }
            } else if (driverRadio.isSelected()) {
                Driver foundDriver = null;
                for (Driver driver : blaBlaCar.getDriverList()) {
                    if (driver.getName().equals(nameField.getText())) {
                        foundDriver = driver;
                        break;
                    }
                }
                if (foundDriver != null) {
                    showDriverInterface(stage, foundDriver);
                    System.out.println("Existing Driver logged in, id: " + foundDriver.getId() + ", name: " + foundDriver.getName());
                } else {
                    Driver newDriver = new Driver(blaBlaCar.getDriverList().size() + 1, nameField.getText());
                    blaBlaCar.registerDriver(newDriver);
                    showDriverInterface(stage, newDriver);
                    System.out.println("New Driver registered, id: " + newDriver.getId() + ", name: " + newDriver.getName());
                }
            }
        });

        layout.getChildren().addAll(registerLabel, passengerRadio, driverRadio, nameField, submitButton);

        Scene scene = new Scene(layout, width, height);
        stage.setScene(scene);
    }

    private void showPassengerInterface(Stage stage, Passenger passenger) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        if (passenger.getName().matches("[0-9]+")) {
            blaBlaCar.blockUser(passenger);
            Label mainLabel = new Label("Ви заблоковані. Ваще ім'я не відповідає вимогам платформи.");
            Button logoutButton = new Button("Вийти");
            logoutButton.setOnAction(e -> start(stage));
            layout.getChildren().addAll(mainLabel, logoutButton);
            System.out.println("User "+passenger.getId()+" "+passenger.getName()+" blocked.");

            Scene scene = new Scene(layout, width, height);
            stage.setScene(scene);
        } else {
            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(10));

            Label mainLabel = new Label("Головна сторінка");
            mainLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Button manageRequestsButton = new Button("Запити");
            manageRequestsButton.setOnAction(e -> showPassengerRequestsInterface(stage, passenger));

            Button logoutButton = new Button("Вийти");
            logoutButton.setOnAction(e -> start(stage));

            header.getChildren().addAll(mainLabel,manageRequestsButton, logoutButton);
            header.setAlignment(Pos.CENTER);

            TextField cityField = new TextField();
            cityField.setPromptText("Оберіть місце призначення");

            Button searchButton = new Button("Пошук");
            ListView<Car> carListView = new ListView<>();
            ObservableList<Car> allCars = FXCollections.observableArrayList(blaBlaCar.getCarList());
            carListView.setItems(allCars);

            searchButton.setOnAction(e -> {
                List<Car> availableCars = passenger.findCar(cityField.getText(), blaBlaCar.getCarList());
                ObservableList<Car> items = FXCollections.observableArrayList(availableCars);
                carListView.setItems(items);
            });

            Button showAllCarsButton = new Button("Показати всі машини");
            showAllCarsButton.setOnAction(e -> carListView.setItems(allCars));

            Button selectButton = new Button("Обрати машину");
            selectButton.setOnAction(e -> {
                Car selectedCar = carListView.getSelectionModel().getSelectedItem();
                if (selectedCar != null) {
                    blaBlaCar.makeRequest(passenger, selectedCar);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Запит відправлено");
                    alert.setHeaderText(null);
                    alert.setContentText("Ваш запит було відправлено водію.");
                    alert.showAndWait();
                    System.out.println("Request send to: " + selectedCar.getDriver() + ", city: "+selectedCar.getCity());
                }
            });

            layout.getChildren().addAll(header, cityField, searchButton,showAllCarsButton, carListView, selectButton);

            Scene scene = new Scene(layout, width, height);
            stage.setScene(scene);
        }
    }
    private void showPassengerRequestsInterface(Stage stage, Passenger passenger){
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));

        Label mainLabel = new Label("Мої запити");
        mainLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> showPassengerInterface(stage, passenger));

        header.getChildren().addAll(mainLabel, backButton);

        ListView<Request> requestListView = new ListView<>();
        ObservableList<Request> requestItems = FXCollections.observableArrayList(passenger.getRequests());
        requestListView.setItems(requestItems);

        requestListView.setCellFactory(param -> new ListCell<Request>() {
            @Override
            protected void updateItem(Request item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    if(item.getIsPaid()){
                        setText("Машина ID: " + item.getCar().getId() + ", Місто: " + item.getCar().getCity() + ", Статус: " + item.getStatus()+". Оплачено");
                    }
                    else{
                        setText("Машина ID: " + item.getCar().getId() + ", Місто: " + item.getCar().getCity() + ", Статус: " + item.getStatus());
                    }
                } else {
                    setText(null);
                }
            }
        });

        requestListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Request selectedRequest = requestListView.getSelectionModel().getSelectedItem();
                if (selectedRequest != null && selectedRequest.getStatus().equals("Approved")) {
                    showPaymentInterface(stage, selectedRequest);
                }
            }
        });

        layout.getChildren().addAll(header, requestListView);

        Scene scene = new Scene(layout, width, height);
        stage.setScene(scene);
    }
    private void showDriverInterface(Stage stage, Driver driver) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        if (driver.getName().matches("[0-9]+")) {
            blaBlaCar.blockUser(driver);
            Label mainLabel = new Label("Ви заблоковані. Ваще ім'я не відповідає вимогам платформи.");
            Button logoutButton = new Button("Вийти");
            logoutButton.setOnAction(e -> start(stage));
            layout.getChildren().addAll(mainLabel, logoutButton);
            System.out.println("User "+driver.getId()+" "+driver.getName()+" blocked.");

            Scene scene = new Scene(layout, width, height);
            stage.setScene(scene);
        }
        else{
            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(10));

            Label mainLabel = new Label("Головна сторінка водія");
            mainLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Button logoutButton = new Button("Вийти");
            logoutButton.setOnAction(e -> start(stage));

            Button manageRequestsButton = new Button("Запити");
            manageRequestsButton.setOnAction(e -> showDriverRequestsInterface(stage, driver));

            header.getChildren().addAll(mainLabel,manageRequestsButton, logoutButton);
            header.setAlignment(Pos.CENTER);

            TextField cityField = new TextField();
            cityField.setPromptText("Місто призначення");

            TextField slotsField = new TextField();
            slotsField.setPromptText("Кількість вільних місць");

            TextField priceField = new TextField();
            priceField.setPromptText("Ціна");

            Button registerCarButton = new Button("Зареєструвати машину");
            registerCarButton.setOnAction(e -> {
                Car car = new Car(blaBlaCar.getCarList().size() + 1, Integer.parseInt(slotsField.getText()), driver.getName(), cityField.getText(), Double.parseDouble(priceField.getText()));
                driver.addCar(car);
                blaBlaCar.addCar(car);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Машину зареєстровано");
                alert.setHeaderText(null);
                alert.setContentText("Вашу машину було успішно зареєстровано.");
                alert.showAndWait();
                System.out.println("Car added: " + car.getId() + ", Driver`s name: " + driver.getName());
            });

            layout.getChildren().addAll(header, cityField, slotsField, priceField, registerCarButton);

            Scene scene = new Scene(layout, width, height);
            stage.setScene(scene);
        }
    }

    private void showDriverRequestsInterface(Stage stage, Driver driver) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));

        Label mainLabel = new Label("Управління запитами");
        mainLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> showDriverInterface(stage, driver));

        header.getChildren().addAll(mainLabel, backButton);
        header.setAlignment(Pos.CENTER);

        ListView<Request> requestListView = new ListView<>();
        ObservableList<Request> requestItems = FXCollections.observableArrayList(driver.getRequests());
        requestListView.setItems(requestItems);

        Button approveButton = new Button("Прийняти");
        approveButton.setOnAction(e -> {
            Request selectedRequest = requestListView.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                driver.approveRequest(selectedRequest);
                requestListView.refresh();

                selectedRequest.setStatus("Approved");

                requestListView.setCellFactory(param -> new ListCell<Request>() {
                    @Override
                    protected void updateItem(Request item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.toString());
                            if (item.getStatus().equals("Approved")) {
                                setDisable(true); // отключаем элемент
                            } else {
                                setStyle(""); // сбрасываем стиль
                                setDisable(false); // включаем элемент
                            }
                        } else {
                            setText(null);
                            setDisable(false); // включаем элемент для пустых ячеек
                        }
                    }
                });

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Запит прийнято");
                alert.setHeaderText(null);
                alert.setContentText("Запит було успішно прийнято.");
                alert.showAndWait();
            }

        });

        Button rejectButton = new Button("Відхилити");
        rejectButton.setOnAction(e -> {
            Request selectedRequest = requestListView.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                driver.rejectRequest(selectedRequest);
                requestListView.refresh();

                selectedRequest.setStatus("Rejected");

                requestListView.setCellFactory(param -> new ListCell<Request>() {
                    @Override
                    protected void updateItem(Request item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.toString());
                            if (item.getStatus().equals("Rejected")) {
                                setDisable(true);
                            } else {
                                setStyle("");
                                setDisable(false);
                            }
                        } else {
                            setText(null);
                            setDisable(false);
                        }
                    }
                });

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Запит відхилено");
                alert.setHeaderText(null);
                alert.setContentText("Запит було відхилено.");
                alert.showAndWait();
            }
        });

        HBox buttons = new HBox(10, approveButton, rejectButton);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(header, requestListView, buttons);

        Scene scene = new Scene(layout, width, height);
        stage.setScene(scene);
    }

    private void showPaymentInterface(Stage stage, Request request) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));

        Label titleLabel = new Label("Оплата поїздки");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> showPassengerRequestsInterface(stage, request.getPassenger()));

        header.getChildren().addAll(titleLabel, backButton);
        header.setAlignment(Pos.CENTER);

        Label infoLabel = new Label("Вибрана машина ID: " + request.getCar().getId() + ", Місто: " + request.getCar().getCity());
        Label priceLabel = new Label("Ціна поїздки: " + request.getCar().getPrice());

        Button payButton = new Button("Оплатити");
        payButton.setOnAction(e -> {
            if(request.getIsPaid()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ви вже оплатили цю поїздку");
                alert.setHeaderText(null);
                alert.setContentText("Оплата поїздки була успішно виконана.");
                alert.showAndWait();
            }
            else{
                blaBlaCar.processPayment(request.getCar());
                request.setIsPaid(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Оплата виконана");
                alert.setHeaderText(null);
                alert.setContentText("Оплата поїздки була успішно виконана.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(header, infoLabel, priceLabel, payButton);

        Scene scene = new Scene(layout, width, height);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        blaBlaCar=new BlaBlaCar();

        Passenger passenger1 = new Passenger(1, "John");
        Passenger passenger2 = new Passenger(2, "Alice");

        Driver driver1 = new Driver(1, "Bob");
        Driver driver2 = new Driver(2, "Charlie");
        Driver driver3 = new Driver(2, "Anna");

        blaBlaCar.registerPassenger(passenger1);
        blaBlaCar.registerPassenger(passenger2);
        blaBlaCar.registerDriver(driver1);
        blaBlaCar.registerDriver(driver2);

        Car car1 = new Car(1, 3, "Bob", "Харків", 100);
        Car car2 = new Car(2, 2, "Charlie", "Львів", 150);
        Car car3=new Car(3,0,"Bob","Херсон",450);
        Car car4=new Car(4,4,driver3.getName(),"Херсон",550);
        Car car5=new Car(5,2,driver3.getName(),"Одеса",300);

        blaBlaCar.addCar(car1);
        blaBlaCar.addCar(car2);
        blaBlaCar.addCar(car3);
        blaBlaCar.addCar(car4);
        blaBlaCar.addCar(car5);

        driver1.addCar(car1);
        driver2.addCar(car2);
        driver2.addCar(car3);
        driver3.addCar(car4);
        driver3.addCar(car5);
        launch();
    }
}