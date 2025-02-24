package edu.iu.habahram.weathermonitoring.model;

import org.springframework.stereotype.Component;

@Component
public class StatisticsDisplay implements Observer, DisplayElement{
    private float avgTemp = 0.00f;
    private float maxTemp = 100.00f;
    private float minTemp = -100.00f;
    private int numberOfObservations = 0;

    private Subject weatherData;

    public StatisticsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public String display() {
        String html = "";
        html += String.format("<div style=\"background-image: " +
                "url(/images/sky.webp); " +
                "height: 400px; " +
                "width: 647.2px;" +
                "display:flex;flex-wrap:wrap;justify-content:center;align-content:center;" +
                "\">");
        html += "<section>";
        html += "<h1>Weather Stats</h1><br />";
        html += String.format("<label>Avg. temp: %s</label><br />", avgTemp);
        html += String.format("<label>Min. temp: %s</label><br />", maxTemp);
        html += String.format("<label>Max. temp: %s</label>", minTemp);
        html += "</section>";
        html += "</div>";
        return html;
    }

    @Override
    public String name() {
        return "Statistics Display";
    }

    @Override
    public String id() {
        return "statistics";
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {

        float temp = (this.avgTemp * this.numberOfObservations + temperature);

        this.numberOfObservations += 1;
        this.avgTemp = temp / this.numberOfObservations;

        if(temperature < this.minTemp){
            this.minTemp = temperature;
        }

        if(temperature > this.maxTemp){
            this.maxTemp = temperature;
        }
    }

    public void subscribe() {
        weatherData.registerObserver(this);
    }

    public void unsubscribe() {
        weatherData.removeObserver(this);
    }
}
