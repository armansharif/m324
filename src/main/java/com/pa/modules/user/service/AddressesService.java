package com.pa.modules.user.service;

import com.pa.modules.user.model.Addresses;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.repository.AddressesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressesService {

    private final AddressesRepository addressesRepository;

    public AddressesService(AddressesRepository addressesRepository) {
        this.addressesRepository = addressesRepository;
    }

    public Addresses saveAddress(Addresses addresses){
        return this.addressesRepository.save(addresses);
    }
    public Optional<Addresses> findById(Long id){
        return this.addressesRepository.findById(id);
    }
    public List<Addresses> findAddressOfUser(Users user){
        return this.addressesRepository.findByUsers(user);
    }

    public void deleteAddresses(Long id) throws Exception{
        this.addressesRepository.deleteById(id);
    }
}
