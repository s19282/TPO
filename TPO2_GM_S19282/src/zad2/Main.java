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
      //naprawić ponowne ok
      primaryStage.setTitle("Webclients");

      VBox mainBox = new VBox();
      mainBox.setAlignment(Pos.BASELINE_CENTER);
      HBox top = new HBox();
      VBox inputData = new VBox();
      VBox rates = new VBox();
      HBox weather = new HBox();
      Label rate1 = new Label();
      Label rate2 = new Label();
      WebView webView = new WebView();
      WebEngine webEngine = webView.getEngine();
      webView.setMaxWidth(1000);
      webView.setStyle("-fx-alignment: center");
      mainBox.setStyle("-fx-background-color: #7574ff;");
      inputData.setStyle("-fx-background-color: #5cff6f;");
      inputData.setPadding(new Insets(10));
      weather.setAlignment(Pos.BASELINE_RIGHT);
      rates.setAlignment(Pos.BASELINE_CENTER);
      //inputData.setMaxSize(150,240);
      top.setStyle("-fx-background-color: #ffb64e;");


      TextField country = new TextField("Enter country");
      TextField city = new TextField("Enter city");
      TextField currency = new TextField("Enter currency");
      Button button = new Button("OK");
      country.setStyle("-fx-background-insets: 5px;");
      city.setStyle("-fx-background-insets: 5px;");
      currency.setStyle("-fx-background-insets: 5px;");
      button.setStyle("-fx-background-insets: 5px;");
      button.setOnAction(event ->
      {
          Service service = new Service(country.getText());
          try
          {
              VBox left = new VBox();
              Label labelL = new Label();
              VBox iconAndDescription = new VBox();
              Label labelC = new Label();
              VBox right = new VBox();
              Label laberR = new Label();
              StringBuilder sb = new StringBuilder();
              JSONObject json = new JSONObject(service.getWeather(city.getText()));
              JSONArray array =  json.getJSONArray("weather");
              String iconID = array.getJSONObject(0).getString("icon");
              Image icon = new Image("http://openweathermap.org/img/wn/"+iconID+"@2x.png");
              labelC.setText(array.getJSONObject(0).getString("description"));
              iconAndDescription.getChildren().addAll(new ImageView(icon),labelC);
              JSONObject important = (JSONObject) json.get("main");
              sb.append("Temperature: ").append(new DecimalFormat("##.0").format(important.getDouble("temp")-273.25))
                      .append("\nTemperature min: ").append(new DecimalFormat("##.0").format(important.getDouble("temp_min")-273.25))
                      .append("\nTemperature max: ").append(new DecimalFormat("##.0").format(important.getDouble("temp_max")-273.25));
              labelL.setText(sb.toString());
              sb = new StringBuilder();
              sb.append("\nFeels like: ").append(new DecimalFormat("##.0").format(important.getDouble("feels_like")-273.25))
                      .append("\nHumidity: ").append(important.getInt("humidity"))
                      .append("\nPressure: ").append(important.getInt("pressure"));
              laberR.setText(sb.toString());
              left.getChildren().addAll(labelL);
              right.getChildren().addAll(laberR);
              weather.getChildren().addAll(left,iconAndDescription,right);
              top.getChildren().add(weather);

          }
          catch (JSONException e)
          {
              weather.getChildren().add(new Label("Oops...there was a problem, restart app or try again"));
              e.printStackTrace();
          }

          if(service.getRateFor(currency.getText())==-1)
            rate1.setText("Oops...there was a problem,\nrestart app or try again");
          else
            rate1.setText("1"+service.getCurrency()+" = "+service.getRateFor(currency.getText())+currency.getText());
          if(service.getNBPRate()==-1)
              rate2.setText("Oops...there was a problem,\nrestart app or try again");
          else
            rate2.setText("1PLN = "+service.getNBPRate()+service.getCurrency());
          webEngine.load("https://en.wikipedia.org/wiki/"+city.getText());
      });

      inputData.getChildren().addAll(country,city,currency,button);
      rates.getChildren().addAll(rate1,rate2);
      top.getChildren().addAll(inputData,rates);
      mainBox.getChildren().addAll(top,webView);
      primaryStage.setScene(new Scene(mainBox, 1280,720));
      primaryStage.setResizable(false);
      primaryStage.show();
  }
}
