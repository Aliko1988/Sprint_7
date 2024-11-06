import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.services.praktikum.scooter.qa.Orders;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

@RunWith(Parameterized.class)
public class OrdersCreatingTests {


    private final Orders orders;

    public OrdersCreatingTests(Orders orders) {
        this.orders = orders;
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{{new Orders("Рон", "Уизли", "ул. Хогвартс 7", "Дубровка", "89854536885", 3, "2024-05-26", "Закинь метлу", new String[]{"BLACK"})}, {new Orders("Седрик", "Диггори", "ул.Косой переулок 10", "Автозаводская", "89999858900", 5, "2024-06-26", "Привези Сову Гарри Поттера", new String[]{"GREY"})}, {new Orders("Гермиона", "Грейнджер", "ул.Тисовая 5", "Арбатская", "89991010101", 7, "2024-07-13", "", new String[]{"BLACK", "GREY"})}, {new Orders("Альбус", "Дамболдор", "ул. Гринготс 1", "Печатники", "89261643333", 3, "2024-01-18", "", new String[]{})},};
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с различными данными")
    public void checkCreateOrder() {
        Response response = given().log().all().header("Content-type", "application/json").body(orders).when().post("/api/v1/orders");
        response.then().log().all().assertThat().and().statusCode(201).body("track", Matchers.notNullValue());
    }
}