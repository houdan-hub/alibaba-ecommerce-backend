package com.alibaba.internship.service.impl;

import com.alibaba.internship.common.CommonException;
import com.alibaba.internship.common.ResultCode;
import com.alibaba.internship.entity.Address;
import com.alibaba.internship.mapper.AddressMapper;
import com.alibaba.internship.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Address getByAddressId(String addressId) {
        Address addr = addressMapper.findByAddressId(addressId);
        if (addr == null) {
            throw new CommonException(ResultCode.ADDRESS_NOT_FOUND);
        }
        return addr;
    }

    @Override
    public List<Address> getByUserId(String userId) {
        return addressMapper.findByUserId(userId);
    }

    @Override
    public Address create(Address address) {
        if (address.getAddressId() == null || address.getAddressId().isBlank()) {
            address.setAddressId("A" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        }
        addressMapper.insert(address);
        return address;
    }

    @Override
    public Address update(Address address) {
        int rows = addressMapper.update(address);
        if (rows == 0) {
            throw new CommonException(ResultCode.ADDRESS_NOT_FOUND);
        }
        return addressMapper.findByAddressId(address.getAddressId());
    }

    @Override
    public void delete(String addressId) {
        int rows = addressMapper.deleteByAddressId(addressId);
        if (rows == 0) {
            throw new CommonException(ResultCode.ADDRESS_NOT_FOUND);
        }
    }
}
