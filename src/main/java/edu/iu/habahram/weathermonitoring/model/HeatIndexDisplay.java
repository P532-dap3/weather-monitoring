package edu.iu.habahram.weathermonitoring.model;

public class HeatIndexDisplay implements Observer, DisplayElement{
    private float heatIndex;

    private Subject weatherData;

    public HeatIndexDisplay(Subject weatherData) {
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
        html += "<h1>Heat Index Display</h1><br/>";
        html += String.format("<label>HeatIndex: %s</label><br />", heatIndex);
        html += "</section>";
        html += "</div>";
        return html;
    }

    private float computeHeatIndex(float t, float rh) {
        float index = (float)((16.923 + (0.185212 * t) + (5.37941 * rh) - (0.100254 * t * rh) +
                (0.00941695 * (t * t)) + (0.00728898 * (rh * rh)) +
                (0.000345372 * (t * t * rh)) - (0.000814971 * (t * rh * rh)) +
                (0.0000102102 * (t * t * rh * rh)) - (0.000038646 * (t * t * t)) + (0.0000291583 *
                (rh * rh * rh)) + (0.00000142721 * (t * t * t * rh)) +
                (0.000000197483 * (t * rh * rh * rh)) - (0.0000000218429 * (t * t * t * rh * rh)) +
                0.000000000843296 * (t * t * rh * rh * rh)) -
                (0.0000000000481975 * (t * t * t * rh * rh * rh)));
        return index;
    }

    public float computeRelativeHumidity(float temperatureC, float absoluteHumidity, float pressure) {
        float temperatureK = (float) (temperatureC + 273.15);

        float es = (float) (6.112f * Math.exp((17.67f * temperatureC) / (temperatureC + 243.5f)));

        float Rv = 461.5f;
        float e = (absoluteHumidity * Rv * temperatureK) / pressure;

        // Compute Relative Humidity (RH)
        float relativeHumidity = (e / es) * 100;

        // Ensure RH is within valid range [0, 100]
        return Math.max(0, Math.min(relativeHumidity, 100));
    }

    @Override
    public String name() {
        return "Heat Index Display";
    }

    @Override
    public String id() {
        return "heat-index";
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.heatIndex = computeHeatIndex(temperature, computeRelativeHumidity(temperature, humidity, pressure));
    }

    public void subscribe() {
        weatherData.registerObserver(this);
    }

    public void unsubscribe() {
        weatherData.removeObserver(this);
    }
}
