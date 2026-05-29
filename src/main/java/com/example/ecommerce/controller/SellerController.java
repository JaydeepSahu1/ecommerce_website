package com.example.ecommerce.controller;

import com.example.ecommerce.Exceptions.SellerException;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.SellerReport;
import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.VerificationCodeRepository;
import com.example.ecommerce.request.LoginRequest;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.response.AuthResponse;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.service.EmailService;
import com.example.ecommerce.service.SellerService;
import com.example.ecommerce.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController
{
    private final SellerService sellerService ;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService  authService;
    private final EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> LoginSeller(@RequestBody LoginRequest req) throws Exception
    {
        String otp= req.getOtp();
        String email = req.getEmail();

        req.setEmail("seller_"+email);

        System.out.println(email + " - " + otp);

        AuthResponse authResponse =authService.signing(req);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp)throws Exception
    {
        VerificationCode verificationCode = verificationCodeRepository.findCodeByOtp(otp);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp))
        {
            throw new Exception("Invalid OTP");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(),otp);

        return new  ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception
    {
        Seller savedSeller =sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject="Mamta Bazar Email Verification Code";
        String text="Welcome to Mamta Bazar , Verivy your Account using this Link";
        String frontend_url = "http://localhost:3000/verify_seller";
        emailService.sendverficationEmail(seller.getEmail(), verificationCode.getOtp(),subject,text+frontend_url);

        return new ResponseEntity<>(savedSeller, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException
    {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerbyJwt(@RequestHeader("Authorization") String jwt) throws Exception
    {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception
//    {
//        Seller seller=sellerService.
//    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required = false)AccountStatus status) throws Exception
    {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                                   @RequestBody Seller seller) throws Exception{

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updateSeller = sellerService.updateSeller(profile.getId(),seller);
        return ResponseEntity.ok(updateSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Seller> deleteSeller(@PathVariable Long id) throws Exception
    {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
