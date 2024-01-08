package com.wteam.framework.modules.member.token;

 import com.wteam.framework.common.security.AuthUser;
 import com.wteam.framework.common.security.enums.UserEnums;
 import com.wteam.framework.common.security.token.Token;
import com.wteam.framework.common.security.token.TokenUtils;
import com.wteam.framework.common.security.token.base.AbstractTokenGenerate;
import com.wteam.framework.modules.member.entity.dos.Member;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 会员token生成
 *
 * @author Chopper
 * @version v4.0
 * @since 2020/11/16 10:50
 */
@Component
public class MemberTokenGenerate extends AbstractTokenGenerate<Member> {
    @Autowired
    private TokenUtils tokenUtil;



    @Override
    public Token createToken(Member member, Boolean longTerm) {
        AuthUser authUser = new AuthUser(member.getUsername(), member.getId(), member.getNickName(), member.getFace(), UserEnums.MEMBER);
        //登陆成功生成token
        return tokenUtil.createToken(member.getNickName(), authUser, longTerm, UserEnums.MEMBER);
    }

    @Override
    public Token refreshToken(String refreshToken) {
        return tokenUtil.refreshToken(refreshToken, UserEnums.MEMBER);
    }

}
