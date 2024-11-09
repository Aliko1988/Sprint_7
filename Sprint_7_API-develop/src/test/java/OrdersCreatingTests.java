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
import com.github.javafaker.Faker;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Step;

@RunWith(Parameterized.class)
public class OrdersCreatingTests {
    private final String colors;
    private final Faker faker = new Faker();

    public OrdersCreatingTests(final String colors) {
        this.colors = colors;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"GREY"},
                {"BLACK"},
                {"BLACK, GREY"},
                {""},
        };
    }


    @Test
    @DisplayName("Создание заказа с разными расцветками самокатов")
    public void testCreateOrder() {
        Orders order = new Orders(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().streetAddress(),
                faker.address().city(),
                faker.phoneNumber().phoneNumber(),
                faker.number().numberBetween(1, 10),
                "07.10.2024",
                faker.address().cityName(),
                colors.split(",")
        );
        Response response = createOrder(order);
        compareStatusCode(response, 201);
        compareBodyCreateOrder(response);
    }


    @Step("создание заказа")
    public Response createOrder(Orders order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("поле трэк не пустой")
    public void compareBodyCreateOrder(Response response) {
        response
                .then()
                .assertThat()
                .body("track", notNullValue());
    }

    @Step("проверка статуса кода")
    public void compareStatusCode(Response response, int code) {
        response
                .then()
                .statusCode(code);
    }




}