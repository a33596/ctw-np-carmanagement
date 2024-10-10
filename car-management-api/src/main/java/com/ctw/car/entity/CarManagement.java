package com.ctw.car.entity;

import com.ctw.car.entity.Car;
import com.ctw.car.entity.EngineType;
import com.ctw.car.entity.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CarManagement extends JFrame {
    private DefaultListModel<String> carListModel;
    private JList<String> carList;
    private JSpinner pickupDateSpinner;
    private JSpinner dropoffDateSpinner;
    private Car car;
    private boolean isCarAvailable;
    private ArrayList<Reservation> reservations;

    public CarManagement() {
        car = new Car(UUID.randomUUID(), "Toyota", "Corolla", EngineType.GASOLINE);
        isCarAvailable = true; // Assume the car is available initially
        reservations = new ArrayList<>(); // List to store reservations

        carListModel = new DefaultListModel<>();
        carList = new JList<>(carListModel);
        updateCarList();

        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(carList), BorderLayout.CENTER);

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Car Management Tool");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Add New Car");
        addButton.addActionListener(new AddButtonListener());
        topPanel.add(addButton);

        JLabel pickupLabel = new JLabel("Pick-up Date/Time:");
        pickupDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor pickupEditor = new JSpinner.DateEditor(pickupDateSpinner, "yyyy/MM/dd HH:mm");
        pickupDateSpinner.setEditor(pickupEditor);
        pickupDateSpinner.setValue(new Date());

        JLabel dropoffLabel = new JLabel("Drop-off Date/Time:");
        dropoffDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dropoffEditor = new JSpinner.DateEditor(dropoffDateSpinner, "yyyy/MM/dd HH:mm");
        dropoffDateSpinner.setEditor(dropoffEditor);
        dropoffDateSpinner.setValue(new Date());

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());

        topPanel.add(pickupLabel);
        topPanel.add(pickupDateSpinner);
        topPanel.add(dropoffLabel);
        topPanel.add(dropoffDateSpinner);
        topPanel.add(searchButton);

        return topPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new RemoveButtonListener());
        buttonPanel.add(removeButton);

        JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ReserveButtonListener());
        buttonPanel.add(reserveButton);

        return buttonPanel;
    }

    private void updateCarList() {
        carListModel.clear();
        if (car != null) {
            String status = isCarAvailable ? "Available" : "Unavailable";
            carListModel.addElement(car.getBrand() + " " + car.getModel() + " (" + car.getEngineType() + ") - " + status);
        }
    }

    private void showReservations(Date pickupDate, Date dropoffDate) {
        StringBuilder reservationList = new StringBuilder();
        for (Reservation reservation : reservations) {
            if (!reservation.getPickupDate().after(dropoffDate) && !reservation.getDropoffDate().before(pickupDate)) {
                reservationList.append("Pick-up: ").append(reservation.getPickupDate())
                        .append(", Drop-off: ").append(reservation.getDropoffDate())
                        .append(", Driver: ").append(reservation.getDriverName()).append("\n");
            }
        }

        if (reservationList.length() == 0) {
            reservationList.append("No reservations for this time slot.");
        }

        JOptionPane.showMessageDialog(this, reservationList.toString(), "Reservations", JOptionPane.INFORMATION_MESSAGE);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField brandField = new JTextField(10);
            JTextField modelField = new JTextField(10);
            JTextField engineTypeField = new JTextField(10);

            JPanel panel = new JPanel();
            panel.add(new JLabel("Brand:"));
            panel.add(brandField);
            panel.add(Box.createHorizontalStrut(15)); // a spacer
            panel.add(new JLabel("Model:"));
            panel.add(modelField);
            panel.add(Box.createHorizontalStrut(15)); // a spacer
            panel.add(new JLabel("Engine Type:"));
            panel.add(engineTypeField);

            int result = JOptionPane.showConfirmDialog(null, panel, 
                     "Please Enter Car Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String brand = brandField.getText();
                String model = modelField.getText();
                String engineType = engineTypeField.getText();

                if (!brand.isEmpty() && !model.isEmpty() && !engineType.isEmpty()) {
                    car = new Car(UUID.randomUUID(), brand, model, EngineType.valueOf(engineType.toUpperCase()));
                    isCarAvailable = true;
                    updateCarList();
                    JOptionPane.showMessageDialog(CarManagement.this, "Car added successfully!");
                } else {
                    JOptionPane.showMessageDialog(CarManagement.this, "Error adding car. Please fill all fields.");
                }
            }
        }
    }

    private class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            car = null;
            updateCarList();
            JOptionPane.showMessageDialog(CarManagement.this, "Car removed successfully!");
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Date pickupDate = (Date) pickupDateSpinner.getValue();
            Date dropoffDate = (Date) dropoffDateSpinner.getValue();
            Date currentDate = new Date();

            if (pickupDate.before(currentDate)) {
                JOptionPane.showMessageDialog(CarManagement.this, "Pick-up date must be the current date or in the future.");
                return;
            }

            // Dummy logic to determine car availability
            isCarAvailable = Math.random() > 0.5;
            updateCarList();

            if (isCarAvailable) {
                showReservations(pickupDate, dropoffDate);
            }
        }
    }

    private class ReserveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (car != null && isCarAvailable) {
                JTextField nameField = new JTextField(10);
                JTextField contactField = new JTextField(10);
                JTextField licenseField = new JTextField(10);

                JPanel panel = new JPanel();
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(Box.createHorizontalStrut(15)); // a spacer
                panel.add(new JLabel("Contact:"));
                panel.add(contactField);
                panel.add(Box.createHorizontalStrut(15)); // a spacer
                panel.add(new JLabel("License Number:"));
                panel.add(licenseField);

                int result = JOptionPane.showConfirmDialog(null, panel, 
                         "Please Enter Driver's Information", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String contact = contactField.getText();
                    String licenseNumber = licenseField.getText();

                    if (!name.isEmpty() && !contact.isEmpty() && !licenseNumber.isEmpty()) {
                        // Dummy logic to simulate reservation success
                        boolean success = Math.random() > 0.2; // 80% chance of success
                        if (success) {
                            reservations.add(new Reservation((Date) pickupDateSpinner.getValue(), (Date) dropoffDateSpinner.getValue(), name));
                            JOptionPane.showMessageDialog(CarManagement.this, "Reservation successful!");
                        } else {
                            JOptionPane.showMessageDialog(CarManagement.this, "Error during reservation. Please try again.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(CarManagement.this, "Error during reservation. Please fill all fields.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(CarManagement.this, "Car is not available for reservation.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarManagement carManagement = new CarManagement();
            carManagement.setVisible(true);
        });
    }
}



class Reservation {
    private Date pickupDate;
    private Date dropoffDate;
    private String driverName;

    public Reservation(Date pickupDate, Date dropoffDate, String driverName) {
        this.pickupDate = pickupDate;
        this.dropoffDate = dropoffDate;
        this.driverName = driverName;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public Date getDropoffDate() {
        return dropoffDate;
    }

    public String getDriverName() {
        return driverName;
    }
}


