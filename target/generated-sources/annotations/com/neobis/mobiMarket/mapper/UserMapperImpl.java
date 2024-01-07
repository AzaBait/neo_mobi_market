package com.neobis.mobiMarket.mapper;

import com.neobis.mobiMarket.dto.RegisterDto;
import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-07T15:11:10+0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto entityToDto(User user) {
        if ( user == null ) {
            return null;
        }

        long id = 0L;
        String username = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String phone = null;
        String profileImage = null;
        String password = null;

        id = user.getId();
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        phone = user.getPhone();
        profileImage = user.getProfileImage();
        password = user.getPassword();

        UserDto userDto = new UserDto( id, username, firstName, lastName, email, phone, profileImage, password );

        return userDto;
    }

    @Override
    public User dtoToEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setUsername( userDto.getUsername() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setEmail( userDto.getEmail() );
        user.setPhone( userDto.getPhone() );
        user.setProfileImage( userDto.getProfileImage() );
        user.setPassword( userDto.getPassword() );

        return user;
    }

    @Override
    public List<UserDto> entitiesToDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    @Override
    public User registerDtoToEntity(RegisterDto registerDto) {
        if ( registerDto == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( registerDto.getUsername() );
        user.setEmail( registerDto.getEmail() );
        user.setPassword( registerDto.getPassword() );

        return user;
    }
}
