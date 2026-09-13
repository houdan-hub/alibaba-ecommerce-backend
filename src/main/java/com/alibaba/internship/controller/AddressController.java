package com.alibaba.internship.controller;

import com.alibaba.internship.common.Result;
import com.alibaba.internship.entity.Address;
import com.alibaba.internship.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{addressId}")
    public Result<Address> getById(@PathVariable String addressId) {
        return Result.success(addressService.getByAddressId(addressId));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Address>> getByUserId(@PathVariable String userId) {
        return Result.success(addressService.getByUserId(userId));
    }

    @PostMapping
    public Result<Address> create(@RequestBody Address address) {
        return Result.success(addressService.create(address));
    }

    @PutMapping
    public Result<Address> update(@RequestBody Address address) {
        return Result.success(addressService.update(address));
    }

    @DeleteMapping("/{addressId}")
    public Result<Void> delete(@PathVariable String addressId) {
        addressService.delete(addressId);
        return Result.success();
    }
}
