package br.gov.economia.apipgdsusep.datasource;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import br.gov.economia.apipgdsusep.utils.AjudantePropriedades;

public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {
	
	private static final String SCHEMA_NAME = (System.getenv("DATABASE_SCHEMA_NAME") != null ? 
			System.getenv("DATABASE_SCHEMA_NAME") : AjudantePropriedades.getProperty(AjudantePropriedades.LOCAL, "pgd.spring.datasource.schema"));
	private static final String TABLE_NAME_ATIVIDADE = "vw_atividade";
	private static final String TABLE_NAME_PACTO = "vw_pacto";
	private static final String TABLE_NAME_PRODUTO = "vw_produto";
	private static final String TABLE_NAME_SITUACAO_PACTO = "vw_situacao_pacto";
	private static final String TABLE_NAME_TIPO_ATIVIDADE = "vw_tipo_atividade";
	private static final String TABLE_NAME_UNIDADE = "vw_unidade";
	

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
    	return identifier;
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertToFullTableName(identifier);
    }

    private Identifier convertToFullTableName(final Identifier identifier) {
    	switch (identifier.getText()) {
    	case TABLE_NAME_ATIVIDADE:
			return new Identifier(SCHEMA_NAME + "." + "dbo" + "." + TABLE_NAME_ATIVIDADE, identifier.isQuoted());
		case TABLE_NAME_PACTO:
			return new Identifier(SCHEMA_NAME + "." + "dbo" + "." + TABLE_NAME_PACTO, identifier.isQuoted());
		case TABLE_NAME_PRODUTO:
			return new Identifier(SCHEMA_NAME + "." + "dbo" + "." + TABLE_NAME_PRODUTO, identifier.isQuoted());
		case TABLE_NAME_SITUACAO_PACTO:
			return new Identifier(SCHEMA_NAME + "." + "dbo" + "." + TABLE_NAME_SITUACAO_PACTO, identifier.isQuoted());
		case TABLE_NAME_TIPO_ATIVIDADE:
			return new Identifier(SCHEMA_NAME + "." + "dbo" + "." + TABLE_NAME_TIPO_ATIVIDADE, identifier.isQuoted());
		case TABLE_NAME_UNIDADE:
			return new Identifier(SCHEMA_NAME + "." + "dbo" + "." + TABLE_NAME_UNIDADE, identifier.isQuoted());
		default:
			return identifier;
		}
    }
}