package br.edu.utfpr.dto;

public class UserDTO {

	private String name;
	private String email;
	private String password;
	private String repassword;

	public UserDTO() {
		// TODO Auto-generated constructor stub
	}

	public UserDTO(String name, String email, String password, String repassword) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.repassword = repassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

}
