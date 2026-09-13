package com.alibaba.internship.service;

import com.alibaba.internship.entity.Address;
import java.util.List;

public interface AddressService {
    Address getByAddressId(String addressId);
    List<Address> getByUserId(String userId);
    Address create(Address address);
    Address update(Address address);
    void delete(String addressId);
}
