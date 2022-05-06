package gov.iti.jets.services;





import java.util.List;

import gov.iti.jets.dtos.UserDto;
import gov.iti.jets.dtos.UserWalletDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;


@WebService
public interface UserService {

    @WebMethod
    public  List<UserDto> getAllUsers();


    @WebMethod
    public Object getUserById( int id);

    @WebMethod
    public String createUser(UserDto userDto);

    @WebMethod
    public String deleteUser( int id);

    @WebMethod
    public String upadateUserWallet(int id,UserWalletDto userWalletDto);
    
    

    
}
