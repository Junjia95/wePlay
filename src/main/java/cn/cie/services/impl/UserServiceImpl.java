package cn.cie.services.impl;

import cn.cie.entity.User;
import cn.cie.entity.Validatecode;
import cn.cie.event.EventModel;
import cn.cie.event.EventProducer;
import cn.cie.event.EventType;
import cn.cie.mapper.UserMapper;
import cn.cie.services.UserService;
import cn.cie.utils.MsgCenter;
import cn.cie.utils.PasswordUtil;
import cn.cie.utils.RedisUtil;
import cn.cie.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public Result register(User user) {
        //验证参数是否合法
        if (StringUtils.isBlank(user.getUsername())) {
            return Result.fail(MsgCenter.EMPTY_USERNAME);
        } else if (StringUtils.isBlank(user.getNickname())) {
            return Result.fail(MsgCenter.EMPTY_NICKNAME);
        } else if (user.getNickname().length() > 10) {
            return Result.fail(MsgCenter.ERROR_NICINAME);
        } else if (StringUtils.isBlank(user.getPassword())) {
            return Result.fail(MsgCenter.EMPTY_PASSWORD);
        } else if (16 < user.getPassword().replaceAll(" ", "").length()
                || user.getPassword().replaceAll(" ", "").length() < 6) {
            return Result.fail(MsgCenter.ERROR_PASSWORD_FORMAT);
        } else if (StringUtils.isBlank(user.getEmail())) {
            return Result.fail(MsgCenter.EMPTY_EMAIL);
        } else if (Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$").
                matcher(user.getEmail()).find() == false) {   // 判断邮箱格式是否正确
            return Result.fail(MsgCenter.ERROR_EMAIL);
        } else if (user.getPhone() == null) {
            return Result.fail(MsgCenter.EMPTY_PHONE);
        } else if (Pattern.compile("1[3|5|7|8|]\\d{9}").matcher(user.getPhone().toString()).find() == false) {  // 验证手机号码是否格式正确
            return Result.fail(MsgCenter.ERROR_PHONE);
        } else if (userMapper.selectByName(user.getUsername()) != null) {                  // 用户名已经被注册
            return Result.fail(MsgCenter.USER_USERNAME_EXISTS);
        } else if (userMapper.selectByEmail(user.getEmail()) != null) {             // 邮箱已被注册
            return Result.fail(MsgCenter.EMAIL_REGISTERED);
        }

        user.setPassword(PasswordUtil.pwd2Md5(user.getPassword().replaceAll(" ", "")));
        if (1 == userMapper.insert(user)){
            String uuid = UUID.randomUUID().toString();
            redisUtil.setStringEx("validatecode_" + user.getId(), uuid, Validatecode.TIMEOUT); //存入redis
            eventProducer.product(new EventModel(EventType.SEND_VALIDATE_EMAIL).setExts("mail", user.getEmail()).setExts("code", uuid));
            return Result.success(user.getId());
        }
        return Result.fail(MsgCenter.ERROR);
    }

    @Override
    public Result sendMail(User user) {
        return null;
    }

    @Override
    public Result validate(Integer uid, String code) {
        return null;
    }

    @Override
    public Result login(String username, String password, boolean remember, String ip, String device) {
        return null;
    }

    @Override
    public Result logout(String token) {
        return null;
    }

    @Override
    public Result updateUserInfo(User user) {
        return null;
    }

    @Override
    public Result updatePassword(String password) {
        return null;
    }

    @Override
    public Result forgetPassword(String password, String email, String code) {
        return null;
    }

    @Override
    public Result sendFetchPwdMail(String email) {
        return null;
    }

    @Override
    public void delNotValidateUser() {

    }

    @Override
    public void expireToken() {

    }
}
