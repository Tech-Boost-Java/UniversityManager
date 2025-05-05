package com.softserve.academy.mapper;

import com.softserve.academy.dto.UserDTO;
import com.softserve.academy.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between User entity and UserDTO.
 */
@Component
public class UserMapper {

    /**
     * Convert User entity to UserDTO.
     * Note: This does not include the password in the DTO for security reasons.
     *
     * @param user the User entity
     * @return the UserDTO
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
        dto.setLastName(user.getLastName());
        return dto;
    }

    /**
     * Convert User entity to UserDTO including password.
     * This should only be used in specific cases where the password is needed.
     *
     * @param user the User entity
     * @return the UserDTO with password
     */
    public UserDTO toDTOWithPassword(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
        dto.setLastName(user.getLastName());
        return dto;
    }

    /**
     * Convert UserDTO to User entity.
     *
     * @param dto the UserDTO
     * @return the User entity
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());

        return user;
    }

    /**
     * Convert a list of User entities to a list of UserDTOs.
     * Note: This does not include passwords in the DTOs for security reasons.
     *
     * @param users the list of User entities
     * @return the list of UserDTOs
     */
    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
