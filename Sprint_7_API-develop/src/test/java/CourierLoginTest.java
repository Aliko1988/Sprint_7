import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.services.praktikum.scooter.qa.Courier;
import ru.services.praktikum.scooter.qa.CourierClient;


public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Курьер авторизирован")
    @Description("Проверка авторизации курьера с корректным логином и паролем")
    public void checkCreatingCourierLoginTest() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCourierLogin = courierClient.getPostRequestCourierLogin(new Courier("HarrPott1", "7777", "harry"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(200).and().body("id", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Курьер авторизирован без логина")
    @Description("Проверка авторизации курьера без ввода логина")
    public void checkVerificationWithoutLoginAuthorization() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCourierLogin = courierClient.getPostRequestCourierLogin(new Courier("", "654321", "germi"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер авторизирован без пароля")
    @Description("Проверка авторизации курьера без ввода пароля")
    public void checkVerificationWithoutPasswordAuthorization() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCourierLogin = courierClient.getPostRequestCourierLogin(new Courier("Ronny", "", "ron"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер авторизирован под несуществующим логином")
    @Description("Проверка авторизации курьера в системе под несуществующим пользователем")
    public void checkAuthorizationUnderIncorrectLogin() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCourierLogin = courierClient.getPostRequestCourierLogin(new Courier("lol", "225566"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер авторизирован под некорректным логином")
    @Description("Проверка авторизации курьера в системе, если неправильно указать логин")
    public void checkEnteringInvalidLogin() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCourierLogin = courierClient.getPostRequestCourierLogin(new Courier("Дигги", "095432"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер авторизирован под некорректным паролем")
    @Description("Проверка авторизации курьера в системе, если неправильно указать пароль")
    public void checkEnteringInvalidPassword() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCourierLogin = courierClient.getPostRequestCourierLogin(new Courier("fake", "ансвер"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }
}