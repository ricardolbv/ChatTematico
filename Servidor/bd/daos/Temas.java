/*Classe Crud de temas 
  - Create - Incluir (); ok
  - Update - Atualizar(); ok
  - Delete - Deletar(); ok
 
  - Read   - Pegar();

*/

package bd.daos;

import java.sql.*;
import bd.*;
import bd.core.*;
import bd.dbos.*;


public class Temas
{
	public static boolean cadastrado (int codigo) throws Exception
	{
		boolean resultado = false;
		
		try 
		{
			String sql;
			sql = "SELECT * "+
				  "FROM TEMAS "+
				  "WHERE CODIGO = ?";
				  
			BDSQLServer.COMANDO.prepareStatement (sql);
			BDSQLServer.COMANDO.setInt(1, codigo);
			
			MeuResultSet result = (MeuResultSet)BDSQLServer.COMANDO.executeQuery();
			
			resultado = result.first();
		}
		catch(SQLException erro)
		{
			throw new Exception ("Busca invalidada pelo BD !");
		}
		
		return resultado;
	}
	
	public static void inlcuir (Tema tema) throws Exception
	{
		if (tema == null)
			throw new Exception ("Objeto invalido");
		
		try
		{
			String sql;
			
			sql = "INSERT INTO "+
				  "TEMAS (CODIGO, NOME) "+
				  "VALUES (?,?)";
				  
			BDSQLServer.COMANDO.prepareStatement(sql);
			
			BDSQLServer.COMANDO.setInt    (1, tema.getCod());
			BDSQLServer.COMANDO.setString (2, tema.getNome());
			
			BDSQLServer.COMANDO.executeUpdate();
			BDSQLServer.COMANDO.commit();
			
		}
		catch (SQLException erro)
		{
			throw new Exception ("Inserção de tema invalido");
		}
	}
	
	
	public static void atualize (Tema tema) throws Exception 
	{
		if (tema == null)
			throw new Exception ("Atualização invalida, objeto null");
		
		if (!cadastrado(tema.getCod())
			throw new Exception ("Livro nao presente no BD, atualização invalida!");
		
		try 
		{
			String sql;
			
			sql = "UPDATE TEMAS"+
				  "SET CODIGO = ? "+
				  "SET NOME   = ? "+
				  "WHERE COD  = ?";
				  
			BDSQLServer.COMANDO.PreparedStatement(sql);
			
			BDSQLServer.COMANDO.setInt(1, tema.getCod());
			BDSQLServer.COMANDO.setInt(2, tema.getNome());
			BDSQLServer.COMANDO.setInt(3, tema.getCod());
			
			BDSQLServer.COMANDO.executeUpdate();
			BDSQLServer.COMANDO.commit();
		}
		catch (SQLException erro)
		{
			throw new Exception ("Erro ao atualizar tema ");
		}
	}
	
	public static void excluir (Tema tema) throws Exception 
	{
		if (tema == null)
			throw new Exception ("Exclusao de tema nulo! ");
		
		if (!cadastrado (tema.getCod()))
			throw new Exception ("Tetativa de exclusao nula");
		
		try
		{
			String sql;
			
			sql = "DELETE FROM TEMAS"+
				  "WHERE CODIGO = ?";
				  
			BDSQLServer.COMANDO.prepareStatement(sql);
			
			BDSQLServer.COMANDO.setInt(1, tema.getCod());
			
			BDSQLServer.COMANDO.executeUpdate();
			BDSQLServer.COMANDO.commit();
		}
		catch (SQLException erro)
		{
			throw new Exception ("Erro de exclusao de tema! ");
		}
	}
	
	
	public static void getTema(int codigo) throws Exception
	{
		Tema tema = null;
		
		if (!cadastrado(codigo))
			throw new Exception ("Codigo de livro nao presente, nao ha o que retornar! ");
		
		try 
		{
			String sql;
			
			sql = "SELECT * FROM TEMAS"+
				  "WHERE CODIGO = ? ";
				  
			BDSQLServer.COMANDO.prepareStatement(sql);
			
			BDSQLServer.COMANDO.setInt(1, codigo);
		
			MeuResultSet retorno = (MeuResultSet)BDSQLServer.COMANDO.executeQuery();
			
			tema = new Tema (retorno.getInt("CODIGO"), retorno.getString("NOME"));
		}
		catch (SQLDataException erro)
		{
			throw new Exception ("Erro ao retornar Tema do BD!");
		}
		
		return tema;
	}
}