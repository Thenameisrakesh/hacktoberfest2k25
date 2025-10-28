import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherAPI {

    // Replace with your OpenWeatherMap API key
    private static final String API_KEY = "YOUR_API_KEY";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter city name: ");
        String city = scanner.nextLine();

        try {
            getWeather(city);
        } catch (Exception e) {
            System.out.println("Error fetching weather: " + e.getMessage());
        }

        scanner.close();
    }

    private static void getWeather(String city) throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject obj = new JSONObject(response.body());
            String cityName = obj.getString("name");
            String country = obj.getJSONObject("sys").getString("country");
            double temp = obj.getJSONObject("main").getDouble("temp");
            String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
            double humidity = obj.getJSONObject("main").getDouble("humidity");
            double windSpeed = obj.getJSONObject("wind").getDouble("speed");

            System.out.println("Weather in " + cityName + ", " + country + ":");
            System.out.println("Temperature: " + temp + "°C");
            System.out.println("Description: " + description);
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + windSpeed + " m/s");
        } else {
            System.out.println("City not found or API error! Status code: " + response.statusCode());
        }
    }
}
