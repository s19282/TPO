/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;


import javafx.application.Application;
import javafx.application.Platform;
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
      rate1.setAlignment(Pos.CENTER_LEFT);
      rate2.setAlignment(Pos.CENTER_LEFT);
      rate1.setStyle("-fx-background-color: #d4ff79;");
      rate2.setStyle("-fx-background-color: #5dffd6;");
      WebView webView = new WebView();
      WebEngine webEngine = webView.getEngine();
      webView.setMaxWidth(1000);
      webView.setStyle("-fx-alignment: center");
      mainBox.setStyle("-fx-background-color: #7574ff;");
      inputData.setStyle("-fx-background-color: #5cff6f;");
      inputData.setPadding(new Insets(10));
      weather.setAlignment(Pos.CENTER_RIGHT);
      weather.setStyle("-fx-font-size: 15;" +
              "-fx-background-color: #7d05ff;");
      weather.setPadding(new Insets(0,0,0,380));
      rates.setAlignment(Pos.CENTER_LEFT);
      rates.setStyle("-fx-font-size: 15;" +
              "-fx-background-color: #4553ff;");
      rates.setPadding(new Insets(0,30,0,30));
      //rates.setStyle("-fx-background-color: #141dff;");
     // weather.setStyle("-fx-background-color: #7d05ff;");

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
      mainBox.getChildren().addAll(top);
      ImageView notFound = new ImageView(new Image(new File("img/notFound.png").toURI().toString()));


      button.setOnAction(event ->
      {
          weather.getChildren().clear();
          mainBox.getChildren().removeAll(notFound,webView);
          Service service = new Service(country.getText());

          if(service.getRateFor(currency.getText())==-1)
              rate1.setText("Oops...there was a problem, restart app or try again");
          else
              rate1.setText("1"+service.getCurrency()+" = "+service.getRateFor(currency.getText())+currency.getText().toUpperCase());
          if(service.getNBPRate()==-1)
              rate2.setText("Oops...there was a problem, restart app or try again");
          else
              rate2.setText("1PLN = "+service.getNBPRate()+service.getCurrency());

          webEngine.load("https://en.wikipedia.org/wiki/"+city.getText());


          try
          {
              VBox left = new VBox();
              Label labelL = new Label();
              VBox iconAndDescription = new VBox();
              iconAndDescription.setStyle("-fx-background-color: #ff0f23;");
              left.setStyle("-fx-background-color: #ff9a06;");
              left.setAlignment(Pos.CENTER);
              Label labelC = new Label();
              VBox right = new VBox();
              Label laberR = new Label();
              right.setAlignment(Pos.CENTER);
              right.setStyle("-fx-background-color: #ff1ec1;");
              StringBuilder sb = new StringBuilder();
              JSONObject json = new JSONObject(service.getWeather(city.getText()));
              JSONArray array =  json.getJSONArray("weather");
              String iconID = array.getJSONObject(0).getString("icon");
              Image icon = new Image("http://openweathermap.org/img/wn/"+iconID+"@2x.png");
              labelC.setText(array.getJSONObject(0).getString("description"));
              iconAndDescription.setAlignment(Pos.CENTER);
              BorderPane bp = new BorderPane();
              bp.setCenter(new ImageView(icon));
              iconAndDescription.getChildren().addAll(bp,labelC);
              JSONObject important = (JSONObject) json.get("main");
              sb.append("Temperature: ").append(new DecimalFormat("##.0").format(important.getDouble("temp")-273.25))
                      .append("\nTemperature min: ").append(new DecimalFormat("##.0").format(important.getDouble("temp_min")-273.25))
                      .append("\nTemperature max: ").append(new DecimalFormat("##.0").format(important.getDouble("temp_max")-273.25));
              labelL.setText(sb.toString());
              sb = new StringBuilder();
              sb.append("Feels like: ").append(new DecimalFormat("##.0").format(important.getDouble("feels_like")-273.25))
                      .append("\nHumidity: ").append(important.getInt("humidity"))
                      .append("\nPressure: ").append(important.getInt("pressure"));
              laberR.setText(sb.toString());
              left.getChildren().addAll(labelL);
              right.getChildren().addAll(laberR);
              weather.getChildren().addAll(left,iconAndDescription,right);

              //mainBox.getChildren().remove(loading);
              mainBox.getChildren().add(webView);


              //mainBox.getChildren().remove(loading);
          }
          catch (JSONException e)
          {
             // mainBox.getChildren().remove(loading);
              weather.setPadding(new Insets(0,0,0,30));
              weather.getChildren().add(new Label("Oops...there was a problem, restart app or try again"));

              mainBox.getChildren().add(notFound);
              System.out.println("Data not found");
          }
      });

      inputData.getChildren().addAll(country,city,currency,button);
      rates.getChildren().addAll(rate1,rate2);
      top.getChildren().addAll(inputData,rates,weather);
      primaryStage.setScene(new Scene(mainBox, 1280,720));
      primaryStage.setResizable(false);
      primaryStage.show();
  }
}
