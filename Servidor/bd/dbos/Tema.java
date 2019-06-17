//Classe DBO do Tema
package bd.dbos;


public class Tema implements Cloneable 
{
	private int 		codigo;
	private String 		nome;
	
	
	public Tema (int codigo, String nome) 
	{
		setNome   (nome);
		setCodigo (codigo);
	}
	
	
	public void setNome 	(String nome)	throws Exception
	{
		if (nome.lenght < 0 || nome.lenght > 15) // Decidimos nao por validação de numeros ou caracteres especiais
			throw new Exception ("Nome Fornecido invalido! ");
			
		this.nome = nome;
	}
	
	public void setCodigo  	(int codigo)	throws Exception
	{
		if (codigo < 0 || codigo > 500) 
			throw new Exception ("Codigo fornecido invalido! ");
		
		this.codigo = codigo;
	}
	
	public int getCod()
	{
		return this.codigo;
	}
	
	public String getNome()
	{
		return this.nome;
	}
	
	
	//Obrigatorios
	public String toString()
	{
		String ret = "";
			ret = "\nNOME: \n"+this.nome
				 +"CODIGO: "+this.codigo;
				 
		return ret;
	}
	
	public boolean equals (Object obj)
	{
		if (obj == null)
			return false;
		
		if (obj.getClass() != this.getClass())
			return false;
		
		if (obj == this)
			return true;
			
		Tema tema = (Tema)obj;
		
		if (!this.nome.equals(tema.nome))
			return false;
		
		if (!this.codigo == tema.codigo)
			return false;
		
		return true;
	}
	
	public int hashCode ()
	{
		ret  = 0;
		
		ret = ret * 3 + this.nome.hashCode();
		ret = ret * 3 + new Integer(this.codigo).hashCode();
		
		return ret;
	}
	
	
	public Object clone() 
	{
		Tema ret; 
		
		try
		{
			ret = new Tema(this);
		}
		catch (Exception erro)
		{//n da erro aqui
		}
		
		return ret;
	}
	
	public Tema (Tema tema) throws Exception
	{
		if (tema == null)
			throw new Exception ("Clone de nulo! ");
		
		this.nome 		= tema.nome;
		this.codigo		= tema.codigo;
	}

}
	