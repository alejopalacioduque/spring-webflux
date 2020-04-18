package com.reactor.academic;

import com.reactor.academic.documment.Rol;
import com.reactor.academic.documment.User;
import com.reactor.academic.repository.IRolRepo;
import com.reactor.academic.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Arrays;

@SpringBootApplication
public class AcademicApplication implements CommandLineRunner {

	@Autowired
	IRolRepo rolRepo;

	@Autowired
	IUserRepo userRepo;

	@Value("${springreactive.username}")
	private String userName;

	@Value("${springreactive.pass}")
	private String pass;

	public static void main(String[] args) {
		SpringApplication.run(AcademicApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String salt = BCrypt.gensalt(12);
		String hashed_password = BCrypt.hashpw(pass, salt);

		rolRepo.deleteAll().subscribe();
		userRepo.deleteAll().subscribe();
		Rol rol = new Rol("ADMIN", "Administrador");
		rolRepo.save(rol).subscribe();
		User user = new User();
		user.setId("001");
		user.setUser(userName);
		user.setPass(hashed_password);
		user.setRoles(Arrays.asList(rol));
		userRepo.save(user).subscribe();
	}

}
