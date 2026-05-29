package com.example.ecommerce.service.impl;

import com.example.ecommerce.Exceptions.SellerException;
import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SellerServiceImpl implements SellerService
{
    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception
    {
        String email=jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception
    {
        Seller sellerExist=sellerRepository.findByEmail(seller.getEmail());
        if(sellerExist!=null)
        {
            throw new Exception("Seller email already exist");
        }
        Address savedAddress=addressRepository.save(seller.getPickupAddress());

        Seller newseller=new Seller();
        newseller.setEmail(seller.getEmail());
        newseller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newseller.setSellerName(seller.getSellerName());
        newseller.setPickupAddress(savedAddress);
        newseller.setGSTIN(seller.getGSTIN());
        newseller.setMobile(seller.getMobile());
        newseller.setRole(seller.getRole());
        newseller.setBankdetails(seller.getBankdetails());
        newseller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newseller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(()-> new SellerException("Seller not found" + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not Found" + email);
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status)
    {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(long id, Seller seller) throws Exception
    {
        Seller existSeller=this.getSellerById(id);

        if(seller.getSellerName() != null)
        {
            existSeller.setSellerName(seller.getSellerName());
        }
        if(seller.getMobile() != null)
        {
            existSeller.setMobile(seller.getMobile());
        }
        if(seller.getEmail() != null)
        {
            existSeller.setEmail(seller.getEmail());
        }
        if(seller.getBusinessDetails() != null
                && seller.getBusinessDetails().getBusinessName() != null)
        {
            existSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        if(seller.getBankdetails() != null
                && seller.getBankdetails().getAccountHolderName() != null
                && seller.getBankdetails().getAccountNumber() != null
                && seller.getBankdetails().getIfscCode() != null)
        {
            existSeller.getBankdetails().setAccountNumber(seller.getBankdetails().getAccountNumber());
            existSeller.getBankdetails().setIfscCode(seller.getBankdetails().getIfscCode());
            existSeller.getBankdetails().setAccountHolderName(seller.getBankdetails().getAccountHolderName());
        }
        if(seller.getPickupAddress() != null
                && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getPinCode() != null
                )
        {
            existSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }
        if(seller.getGSTIN() != null)
        {
            existSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existSeller);
    }

    @Override
    public void deleteSeller(long id) throws Exception {

        Seller seller=getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerfied(true);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);
        seller.setAccountStatus(status);

        return sellerRepository.save(seller);
    }
}
