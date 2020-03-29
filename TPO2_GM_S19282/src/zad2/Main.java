/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.text.DecimalFormat;

public class Main extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
      primaryStage.setTitle("Webclients");

      VBox mainBox = new VBox();
      HBox top = new HBox();
      VBox inputData = new VBox();
      VBox rates = new VBox();
      HBox weather = new HBox();
      WebView webView = new WebView();
      WebEngine webEngine = webView.getEngine();
      TextField country = new TextField("Enter country");
      TextField city = new TextField("Enter city");
      TextField currency = new TextField("Enter currency");
      Button button = new Button("OK");
      ImageView notFound = new ImageView(new Image(new File("img/notFound.png").toURI().toString()));

      mainBox.setStyle("-fx-background-color: rgba(76,108,255,0.69);");
      mainBox.setAlignment(Pos.BASELINE_CENTER);
      top.setPadding(new Insets(0,0,20,0));
      inputData.setPadding(new Insets(10));
      weather.setStyle("-fx-font-size: 15;");
      weather.setPadding(new Insets(0,0,0,380));
      rates.setAlignment(Pos.CENTER_LEFT);
      rates.setStyle("-fx-font-size: 15;");
      rates.setPadding(new Insets(0,30,0,30));
      webView.setMaxWidth(1000);
      country.setStyle("-fx-background-insets: 5px;");
      city.setStyle("-fx-background-insets: 5px;");
      currency.setStyle("-fx-background-insets: 5px;");
      button.setStyle("-fx-background-insets: 5px;");
      button.setOnAction(event ->
      {
          Service service = new Service(country.getText());
          Label rate1 = new Label();
          Label rate2 = new Label();

          weather.getChildren().clear();
          rates.getChildren().clear();
          mainBox.getChildren().removeAll(notFound,webView);
          rate1.setAlignment(Pos.CENTER_LEFT);
          rate2.setAlignment(Pos.CENTER_LEFT);
          webEngine.load("https://en.wikipedia.org/wiki/"+city.getText());

          if(service.getRateFor(currency.getText())==-1)
              rate1.setText("Oops...there was a problem, restart app or try again");
          else
              rate1.setText("1"+service.getCurrency()+" = "+service.getRateFor(currency.getText())+currency.getText().toUpperCase());

          if(service.getNBPRate()==-1)
              rate2.setText("Oops...there was a problem, restart app or try again");
          else
              rate2.setText("1PLN = "+service.getNBPRate()+service.getCurrency());

          rates.getChildren().addAll(rate1,rate2);

          try
          {
              VBox left = new VBox();
              VBox iconAndDescription = new VBox();
              VBox right = new VBox();
              Label labelL = new Label();
              Label labelC = new Label();
              Label laberR = new Label();
              StringBuilder sb = new StringBuilder();
              JSONObject json = new JSONObject(service.getWeather(city.getText()));
              JSONArray array =  json.getJSONArray("weather");
              String iconID = array.getJSONObject(0).getString("icon");
              Image icon = new Image("http://openweathermap.org/img/wn/"+iconID+"@2x.png");
              BorderPane bp = new BorderPane();
              JSONObject important = (JSONObject) json.get("main");

              left.setAlignment(Pos.CENTER);
              right.setAlignment(Pos.CENTER);
              iconAndDescription.setAlignment(Pos.CENTER);
              labelC.setText(array.getJSONObject(0).getString("description"));
              bp.setCenter(new ImageView(icon));
              sb.append("Temperature: ").append(new DecimalFormat("##.0").format(important.getDouble("temp")-273.25))
                      .append("\nTemperature min: ").append(new DecimalFormat("##.0").format(important.getDouble("temp_min")-273.25))
                      .append("\nTemperature max: ").append(new DecimalFormat("##.0").format(important.getDouble("temp_max")-273.25));
              labelL.setText(sb.toString());
              sb = new StringBuilder();
              sb.append("Feels like: ").append(new DecimalFormat("##.0").format(important.getDouble("feels_like")-273.25))
                      .append("\nHumidity: ").append(important.getInt("humidity"))
                      .append("\nPressure: ").append(important.getInt("pressure"));
              laberR.setText(sb.toString());
              left.getChildren().add(labelL);
              iconAndDescription.getChildren().addAll(bp,labelC);
              right.getChildren().add(laberR);
              weather.getChildren().addAll(left,iconAndDescription,right);
              mainBox.getChildren().add(webView);
          }
          catch (JSONException e)
          {
              weather.setPadding(new Insets(0,0,0,30));
              weather.getChildren().add(new Label("Oops...there was a problem, restart app or try again"));
              mainBox.getChildren().add(notFound);
              System.out.println("Data not found");
          }
      });

      inputData.getChildren().addAll(country,city,currency,button);
      top.getChildren().addAll(inputData,rates,weather);
      mainBox.getChildren().addAll(top);
      primaryStage.setScene(new Scene(mainBox, 1280,720));
      primaryStage.setResizable(false);
      primaryStage.show();
  }
}
