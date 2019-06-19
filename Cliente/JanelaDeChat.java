import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class JanelaDeChat extends    JFrame
                   implements WindowListener,
                              MouseListener,
                              FocusListener,
                              KeyEventDispatcher
{
    private static final long serialVersionUID = 2423272621216229844L;

	private String            nick;
    private Parceiro          servidor;
	private String 			  tema;
    private ArrayList<String> usuarios;

    private JButton btnEnviar =
    new JButton ("Enviar");
    private JButton btnSair =
    new JButton ("Sair");
    private JTextField txfMensagemAEnviar =
    new JTextField ();
    private JTextPane txpMensagensRecebidas =
    new JTextPane ();
    private DefaultListModel<String> modUsuarios =
    new DefaultListModel<String> ();
    private JList<String> lstUsuarios =
    new JList<String> (this.modUsuarios);

    public JanelaDeChat (String nick, Parceiro servidor, String tema)
    throws Exception
    {
        if (nick==null || nick.equals(""))
            throw new Exception ("Nick ausente");

        this.nick = nick;

        if (servidor==null)
            throw new Exception ("Servidor ausente");
		
		this.servidor = servidor;
		
		if (tema==null)
			throw new Exception ("Tema Ausente");

        this.tema = tema;

        this.setTitle ("USUARIO: "+nick.toUpperCase() +"   TEMA: "+tema.toUpperCase());
        this.setSize  (700, 490);

        Font fntPtFixa  = new Font ("Arial", Font.BOLD,  16);
        Font fntPtVar   = new Font ("Arial", Font.PLAIN, 13);

        this.btnEnviar            .setFont (fntPtFixa);
        this.btnSair              .setFont (fntPtFixa);
        lstUsuarios               .setFont (fntPtVar);
        this.txpMensagensRecebidas.setFont (fntPtVar);
        txfMensagemAEnviar        .setFont (fntPtVar);

        JLabel lblMensagensRecebidas = new JLabel ("Mensagens recebidas:");
        lblMensagensRecebidas.setFont (fntPtFixa);

        JLabel lblUsuarios = new JLabel ("Usuarios:");
        lblUsuarios.setFont (fntPtFixa);

        JLabel lblMensagemAEnviar = new JLabel ("Mensagem a Enviar:");
        lblMensagemAEnviar.setFont (fntPtFixa);

        this.txpMensagensRecebidas.setEditable (false);

        lstUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrUsuarios = new JScrollPane(lstUsuarios);
        modUsuarios.addElement ("TODOS");
        lstUsuarios.setSelectedIndex(0);

        this.setLayout (new BorderLayout ());

        JPanel pnlUsuarios = new JPanel ();
        pnlUsuarios.setLayout (new BorderLayout ());

        JPanel pnlAreaDeTransmissao = new JPanel ();
        pnlAreaDeTransmissao.setLayout (new GridLayout (3,1));

        pnlUsuarios.add ("North",  lblUsuarios);
        pnlUsuarios.add ("Center", scrUsuarios);
        pnlUsuarios.add ("South",  this.btnSair);

        pnlAreaDeTransmissao.add (lblMensagemAEnviar);
        pnlAreaDeTransmissao.add (txfMensagemAEnviar);
        pnlAreaDeTransmissao.add (this.btnEnviar);

        this.add ("North",  lblMensagensRecebidas);
        this.add ("Center", new JScrollPane(this.txpMensagensRecebidas));
        this.add ("East",   pnlUsuarios);
        this.add ("South",  pnlAreaDeTransmissao);

        this                   .addWindowListener (this);
        this.txfMensagemAEnviar.addFocusListener  (this);
        this.btnEnviar         .addMouseListener  (this);
        this.btnSair           .addMouseListener  (this);

        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        this.setLocationRelativeTo (null); // centraliza

        this.setVisible (true);

        txfMensagemAEnviar.requestFocus();
    }

    public void novoUsuario (String nick) throws Exception
    {
        if (nick==null)
            throw new Exception ("Nick ausente");

        this.modUsuarios.setSize(modUsuarios.getSize()+1);

        int i;
        for (i=this.modUsuarios.size()-2;
             i>0 && nick.compareTo(this.modUsuarios.get(i))<0;
             i--)
        {
            this.modUsuarios.set(i+1,this.modUsuarios.get(i));
        }
        this.modUsuarios.set(i+1,nick);
    }

    public void removaUsuario (String nick) throws Exception
    {
        if (nick==null)
            throw new Exception ("Nick ausente");

        this.modUsuarios.removeElement (nick);
    }

    public void novaMensagem (String remetente, String texto) throws Exception
    {
        if (remetente==null)
            throw new Exception ("Remetente ausente");

        if (texto==null)
            throw new Exception ("Texto ausente");

        AttributeSet preto    = StyleContext.getDefaultStyleContext().addAttribute(
                                SimpleAttributeSet.EMPTY,
                                StyleConstants.Foreground,
                                Color.BLACK);

        AttributeSet vermelho = StyleContext.getDefaultStyleContext().addAttribute(
                                SimpleAttributeSet.EMPTY,
                                StyleConstants.Foreground,
                                Color.RED);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        "("+new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss").format(new Date())+") ",
        preto);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        remetente,
        vermelho);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        " "+texto+"\n\n",
        preto);

        this.txpMensagensRecebidas.setCaretPosition(this.txpMensagensRecebidas.getDocument().getLength());
    }

    public void novaMensagem (String remetente, String destinatario, String texto)
    throws Exception
    {
        if (remetente==null)
            throw new Exception ("Remetente ausente");

        if (destinatario==null)
            throw new Exception ("Destinatario ausente");

        if (texto==null)
            throw new Exception ("Texto ausente");

        AttributeSet preto    = StyleContext.getDefaultStyleContext().addAttribute(
                                SimpleAttributeSet.EMPTY,
                                StyleConstants.Foreground,
                                Color.BLACK);

        AttributeSet vermelho = StyleContext.getDefaultStyleContext().addAttribute(
                                SimpleAttributeSet.EMPTY,
                                StyleConstants.Foreground,
                                Color.RED);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        "("+new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss").format(new Date())+") ",
        preto);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        remetente,
        vermelho);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        " diz para ",
        preto);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        destinatario,
        vermelho);

        this.txpMensagensRecebidas.getDocument().insertString (
        txpMensagensRecebidas.getDocument().getLength(),
        ": "+texto+"\n\n",
        preto);

        this.txpMensagensRecebidas.setCaretPosition(
		this.txpMensagensRecebidas.getDocument().getLength());
    }





    public void windowClosing (WindowEvent evt)
    {
        trateClickEmSair ();
    }

    public void windowOpened (WindowEvent evt)
    {}

    public void windowClosed (WindowEvent evt)
    {}

    public void windowActivated (WindowEvent evt)
    {}

    public void windowDeactivated (WindowEvent evt)
    {}

    public void windowIconified (WindowEvent evt)
    {}

    public void windowDeiconified (WindowEvent evt)
    {}




    private void trateClickEmEnviar ()
    {
		try
		{
			if (!this.lstUsuarios.isSelectionEmpty() &&
				!txfMensagemAEnviar.getText().equals(""))
			{
				servidor.receba (
				new Comunicado (
				"ENC",
				this.nick,                               // remetente
				lstUsuarios       .getSelectedValue(),   // destinatario
				txfMensagemAEnviar.getText         ())); // texto da mensagem

				this.novaMensagem ("Você",                                // remetente
								   lstUsuarios       .getSelectedValue(), // destinatario
								   txfMensagemAEnviar.getText         ());
			}
		}
		catch (Exception erro)
		{
			JOptionPane.showMessageDialog(null/*sem janela mãe*/,
			"Tente novamente mais tarde!",
			"Erro de conectividade",
			JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}

		this.txfMensagemAEnviar.setText ("");
    }

    private void trateClickEmSair ()
    {
		try
		{
			this.servidor.receba (new Comunicado ("SAI"));
			this.servidor.adeus  ();
		}
		catch (Exception erro)
		{
			JOptionPane.showMessageDialog(null/*sem janela mãe*/,
			"Tente novamente mais tarde!",
			"Erro de conectividade",
			JOptionPane.ERROR_MESSAGE);
		}

		System.exit(0);
    }

    public void mouseClicked (MouseEvent evt)
    {
        if (evt.getButton       () == MouseEvent.BUTTON1 &&
            evt.getClickCount   () == 1                  &&
           !evt.isAltDown       ()                       &&
           !evt.isAltGraphDown  ()                       &&
           !evt.isControlDown   ()                       &&
           !evt.isMetaDown      ()                       &&
           !evt.isShiftDown     ())
            if (evt.getComponent() == btnEnviar)
                this.trateClickEmEnviar ();
            else
                if (evt.getComponent() == btnSair)
                    this.trateClickEmSair ();
    }

    public void mousePressed (MouseEvent evt)
    {}

    public void mouseReleased (MouseEvent evt)
    {}

    public void mouseEntered (MouseEvent evt)
    {}

    public void mouseExited (MouseEvent evt)
    {}




    public void focusGained (FocusEvent evt)
    {}

    public void focusLost (FocusEvent evt)
    {
        this.txfMensagemAEnviar.requestFocus ();
    }




    public boolean dispatchKeyEvent(KeyEvent evt)
    {
        if (evt.getID() == KeyEvent.KEY_PRESSED)
            if (evt.getKeyCode() == KeyEvent.VK_ENTER)
                this.trateClickEmEnviar();

        return false; // continua a acao normalmente
    }
}
