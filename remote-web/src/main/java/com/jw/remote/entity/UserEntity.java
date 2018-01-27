package com.jw.remote.entity;

/**
 * 用户信息实体
 * @author Administrator
 *
 */
public class UserEntity {

	public static final int LIMITS_NORMAL = 0;
	public static final int LIMITS_WHITE = 1;
	public static final int LIMITS_BLACK = 2;
	
	private long id;
	private String phone;
	private  int sex = 1;   //1male 0-female
	private String username;
	private String password;
	private int limits = 0;  //0-普通  1-白名单  2-黑名单
	private String weixin;
	private String touxiang;
	private String zone;  //地区ini
	private String sign;  //签名信息
	private int prov = -1; //省份
	private int city = -1; //城市
	private int version ;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLimits() {
		return limits;
	}
	public void setLimits(int limits) {
		this.limits = limits;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public int getProv() {
		return prov;
	}
	public void setProv(int prov) {
		this.prov = prov;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}
