package com.heyi.framework.session.ltpatoken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.heyi.framework.session.Config;
import com.heyi.utils.Base64;

public final class LtpaToken {
	private byte[] creation;
	private Date creationDate;
	private byte[] digest;
	private byte[] expires;
	private Date expiresDate;
	private byte[] hash;
	private byte[] header;
	private String ltpaToken;
	private byte[] rawToken;
	private byte[] user;

	public LtpaToken(String token) {
		init();
		this.ltpaToken = token;
		this.rawToken = Base64.decode(token);
		this.user = new byte[this.rawToken.length - 40];
		for (int i = 0; i < 4; i++) {
			this.header[i] = this.rawToken[i];
		}
		for (int i = 4; i < 12; i++) {
			this.creation[(i - 4)] = this.rawToken[i];
		}
		for (int i = 12; i < 20; i++) {
			this.expires[(i - 12)] = this.rawToken[i];
		}
		for (int i = 20; i < this.rawToken.length - 20; i++) {
			this.user[(i - 20)] = this.rawToken[i];
		}
		for (int i = this.rawToken.length - 20; i < this.rawToken.length; i++) {
			this.digest[(i - (this.rawToken.length - 20))] = this.rawToken[i];
		}
		this.creationDate = new Date(Long.parseLong(new String(this.creation),
				16) * 1000L);
		this.expiresDate = new Date(
				Long.parseLong(new String(this.expires), 16) * 1000L);
	}

	private LtpaToken() {
		init();
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public Date getExpiresDate() {
		return this.expiresDate;
	}

	public String getUser() {
		return new String(this.user);
	}

	public boolean isValid() {
		boolean validDigest = false;
		boolean validDateRange = false;

		byte[] bytes = (byte[]) null;
		Date now = new Date();

		MessageDigest md = getDigest();

		bytes = concatenate(bytes, this.header);

		bytes = concatenate(bytes, this.creation);

		bytes = concatenate(bytes, this.expires);

		bytes = concatenate(bytes, this.user);

		bytes = concatenate(bytes, Base64.decode(Config.SECRET));

		byte[] newDigest = md.digest(bytes);

		validDigest = MessageDigest.isEqual(this.digest, newDigest);

		validDateRange =
				//(now.after(this.creationDate)) &&  //创建token上面的时钟有可能和验证token的服务器时钟有出入，就会造成创建时期比较不成功
				(now.before(this.expiresDate));

		return validDigest & validDateRange;
	}

	public String toString() {
		return this.ltpaToken;
	}

	private MessageDigest getDigest() {
		try {
			return MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return null;
	}

	private void init() {
		this.creation = new byte[8];
		this.digest = new byte[20];
		this.expires = new byte[8];
		this.hash = new byte[20];
		this.header = new byte[4];
	}

	public static boolean isValid(String ltpaToken) {
		LtpaToken ltpa = new LtpaToken(ltpaToken);
		return ltpa.isValid();
	}

	public static String generate(String userInfo) {
		return generate(userInfo, null, null);
	}

	public static String generate(String userInfo, Date tokenCreation) {
		return generate(userInfo, tokenCreation, null);
	}

	public static String generate(String userInfo, Date tokenCreation,
			Date tokenExpires) {
		LtpaToken ltpa = new LtpaToken();
		if (tokenCreation == null) {
			tokenCreation = new Date();
		}
		if (tokenExpires == null) {
			tokenExpires = new Date(tokenCreation.getTime()
					+ TimeUnit.MINUTES.toMillis(Config.EXPIRATION));
		}

		Calendar calendar = Calendar.getInstance();
		MessageDigest md = ltpa.getDigest();
		ltpa.header = new byte[] { 0, 1, 2, 3 };
		ltpa.user = userInfo.getBytes();
		byte[] token = (byte[]) null;
		calendar.setTime(tokenCreation);
		ltpa.creation = Long.toHexString(calendar.getTimeInMillis() / 1000L)
				.toUpperCase().getBytes();
		calendar.setTime(tokenExpires);
		ltpa.expires = Long.toHexString(calendar.getTimeInMillis() / 1000L)
				.toUpperCase().getBytes();
		ltpa.user = userInfo.getBytes();
		token = concatenate(token, ltpa.header);
		token = concatenate(token, ltpa.creation);
		token = concatenate(token, ltpa.expires);
		token = concatenate(token, ltpa.user);
		md.update(token);
		ltpa.digest = md.digest(Base64.decode(Config.SECRET));
		token = concatenate(token, ltpa.digest);
		return new String(Base64.encode(token));
	}

	private static byte[] concatenate(byte[] a, byte[] b) {
		if (a == null) {
			return b;
		}
		byte[] bytes = new byte[a.length + b.length];

		System.arraycopy(a, 0, bytes, 0, a.length);
		System.arraycopy(b, 0, bytes, a.length, b.length);
		return bytes;
	}

	public String getLtpaToken() {
		return this.ltpaToken;
	}

	public void setLtpaToken(String ltpaToken) {
		this.ltpaToken = ltpaToken;
	}
	
	public static void main(String[] args) {
		LtpaToken token = new LtpaToken("AAECAzU4QzVFNTVBNThFRDcyNUF7InMiOiIxOTA5MiIsInUiOiIwRThGRDhBODlBRENEMjFEQjhFOEY4NDRBQTJGNEU0MSJ94UOzh6lVnyIWE4EGr+acw75lVF8=");
		
		System.out.println(token.getUser());
	}
}