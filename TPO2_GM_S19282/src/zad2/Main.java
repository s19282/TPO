/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;


import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
      Label weather = new Label();
      Label rate1 = new Label();
      Label rate2 = new Label();
      WebView webView = new WebView();
      WebEngine webEngine = webView.getEngine();


      primaryStage.setTitle("Webclients");
      HBox hBox = new HBox();
      VBox inputData = new VBox();


      TextField country = new TextField("Enter country");
      TextField city = new TextField("Enter city");
      TextField currency = new TextField("Enter currency");
      Button button = new Button("OK");
      button.setOnAction(event ->
      {
          Service service = new Service(country.getText());
          weather.setText(service.getWeather(city.getText()));
          rate1.setText(String.valueOf(service.getRateFor(currency.getText())));
          rate2.setText(String.valueOf(service.getNBPRate()));
          webEngine.load("https://en.wikipedia.org/wiki/"+city.getText());
      });


      inputData.getChildren().addAll(country,city,currency,button,weather,rate1,rate2,webView);

      hBox.getChildren().addAll(inputData);
      primaryStage.setScene(new Scene(hBox, 1280,720));
      primaryStage.setResizable(false);
      primaryStage.show();
  }
}
