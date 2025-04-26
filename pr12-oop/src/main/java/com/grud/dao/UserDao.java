package com.grud.dao;

import com.grud.entity.User;
import com.grud.util.ConnectionManager;
import com.grud.exception.DaoException;
import java.sql.*;
import java.util.Optional;

public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    private static final String INSERT_SQL = """
        INSERT INTO users (name, email, phone, address, date_of_birth, status)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, name, email, phone, address, date_of_birth, status
        FROM users
        WHERE id = ?
        """;

    private static final String DELETE_SQL = """
        DELETE FROM users
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE users
        SET name = ?, email = ?, phone = ?, address = ?, date_of_birth = ?, status = ?
        WHERE id = ?
        """;

    private UserDao() {}

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public User insert(User user) {
        try (Connection connection = ConnectionManager.open();
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setDate(5, Date.valueOf(user.getDateOfBirth()));
            ps.setString(6, user.getStatus());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }

            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(int id) {
        try (Connection connection = ConnectionManager.open();
            PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(User user) {
        try (Connection connection = ConnectionManager.open();
            PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setDate(5, Date.valueOf(user.getDateOfBirth()));
            ps.setString(6, user.getStatus());
            ps.setInt(7, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<User> selectById(int id) {
        try (Connection connection = ConnectionManager.open();
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            User user = null;
            if (rs.next()) {
                user = buildUser(rs);
            }

            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private User buildUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getDate("date_of_birth").toLocalDate(),
            rs.getString("status")
        );
    }

    public boolean updateProfileImage(int userId, byte[] imageBytes) {
        String updateImageSql = "UPDATE users SET profile_image = ? WHERE id = ?";
        try (Connection connection = ConnectionManager.open();
            PreparedStatement ps = connection.prepareStatement(updateImageSql)) {

            ps.setBytes(1, imageBytes);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<byte[]> getProfileImage(int userId) {
        String selectImageSql = "SELECT profile_image FROM users WHERE id = ?";
        try (Connection connection = ConnectionManager.open();
            PreparedStatement ps = connection.prepareStatement(selectImageSql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("profile_image");
                return Optional.ofNullable(imageBytes);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}
