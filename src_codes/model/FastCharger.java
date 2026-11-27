
package model;

// FastCharger inherits from Charger
public class FastCharger extends Charger {

    // Constructor
    public FastCharger(String chargerId, double chargingPower) {
        super(chargerId, chargingPower);
    }

    // Display fast charger details
    @Override
    public void displayChargerDetails() {
        System.out.println("Charger Type: Fast Charger");
        System.out.println("Charger ID: " + getChargerId());
        System.out.println("Charging Power: " + getChargingPower() + " kW");
    }
}
