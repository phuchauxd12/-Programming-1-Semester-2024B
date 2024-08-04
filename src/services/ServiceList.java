package services;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ServiceList {
    private List<Service> services;

    public ServiceList() {
        this.services = new ArrayList<>();
    }

    public void addService(Service service) {
        services.add(service);
    }

    public Service getServiceById(String serviceId) {
        for (Service service : services) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    public List<Service> getAllServices() {
        return services;
    }

    public void updateService(Service updatedService) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getServiceId().equals(updatedService.getServiceId())) {
                services.set(i, updatedService);
                break;
            }
        }
    }

    public void deleteService(String serviceId) {
        services.removeIf(service -> service.getServiceId().equals(serviceId));
    }

    public List<Service> getServicesBetween(LocalDate startDate, LocalDate endDate) {
        List<Service> filteredServices = new ArrayList<>();
        for (Service service : services) {
            if ((service.getServiceDate().isEqual(startDate) || service.getServiceDate().isAfter(startDate)) &&
                    (service.getServiceDate().isEqual(endDate) || service.getServiceDate().isBefore(endDate))) {
                filteredServices.add(service);
            }
        }
        return filteredServices;
    }
}