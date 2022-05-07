package gov.iti.jets.services;

import java.util.List;
import gov.iti.jets.dtos.UserDto;
import gov.iti.jets.dtos.UserWalletDto;
import gov.iti.jets.exceptions.NotFoundDtoException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface UserService {

    @WebMethod
    public List<UserDto> getAllUsers() throws NotFoundDtoException;

    @WebMethod
    public UserDto getUserById(int id)throws NotFoundDtoException;

    @WebMethod
    public String createUser(UserDto userDto);

    @WebMethod
    public String deleteUser(int id);

    @WebMethod
    public String upadateUserWallet(int id, UserWalletDto userWalletDto);

    

}
