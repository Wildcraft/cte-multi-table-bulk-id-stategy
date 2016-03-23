package br.com.evandropires.hibernate.ctestrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Vlad Mihalcea
 */
public class CTETest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
			Person.class,
			Doctor.class,
			Engineer.class
		};
	}

	@Override
	protected Configuration constructConfiguration() {
		Configuration configuration = super.constructConfiguration();
		configuration.setProperty( AvailableSettings.HQL_BULK_ID_STRATEGY, CTEMultiTableBulkIdStrategy.class.getName() );
		configuration.setProperty( AvailableSettings.SHOW_SQL, "true" );
		return configuration;
	}

	@Test
	public void test() {
		Session session = openSession();
		Transaction transaction = session.beginTransaction();
		Doctor doctor = new Doctor();
		session.save( doctor );
		Engineer engineer = new Engineer();
		session.save( engineer );
		session.flush();
		session.createQuery( "delete from Person" ).executeUpdate();
		transaction.commit();
		session.close();
	}

	@Entity(name = "Person")
	@Inheritance(strategy = InheritanceType.JOINED)
	public static class Person {

		@Id
		@GeneratedValue
		private Long id;
	}

	@Entity(name = "Doctor")
	public static class Doctor extends Person {

	}

	@Entity(name = "Engineer")
	public static class Engineer extends Person {

	}
}