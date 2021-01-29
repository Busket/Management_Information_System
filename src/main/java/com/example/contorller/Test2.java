package com.example.contorller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test2 {
    public static void main(String[] args) {
        String compactJws="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwIiwic3ViIjoiREFVc2VyIiwiaWF0IjoxNTk1MDYzOTg0LCJlbWFpbCI6Imd6cXp6cjQ0QDE2My5jb20iLCJwYXNzd29yZCI6IjBmZTQyYmQ0OTE0NDdmYzRhODhlMWZiOTAwYWZiZGQ2IiwiZXhwIjoxNTk1MDY3NTg0fQ.z07Li_njpndmojnA3Qb8lhZE4FR08HflDO4NRNPuGvk";
        Claims claims = Jwts.parser().setSigningKey("wangmh").parseClaimsJws(compactJws).getBody();
        System.out.println("id:"+claims.getId());
        System.out.println("subject:"+claims.getSubject());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy‐MM‐dd hh:mm:ss");
        System.out.println("签发时间:"+sdf.format(claims.getIssuedAt()));
        System.out.println("过期时间:"+sdf.format(claims.getExpiration()));
        System.out.println("当前时间:"+sdf.format(new Date()) );
        System.out.println("email:"+claims.get("email"));
        System.out.println("pwd:"+claims.get("password"));
    }

}
