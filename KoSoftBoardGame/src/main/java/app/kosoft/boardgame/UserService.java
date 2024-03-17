package app.kosoft.boardgame;

import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();







}
