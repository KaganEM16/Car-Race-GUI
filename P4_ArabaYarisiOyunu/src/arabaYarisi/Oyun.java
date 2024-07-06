package arabaYarisi;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Oyun extends JFrame implements ActionListener
{
	Araba[] arabaGorevleri;
	JPanel[] paneller;
	JButton[] yol;
	ArrayList<Integer> baslangicKonumu = new ArrayList<>();
	ArrayList<Integer> bitisKonumu = new ArrayList<>();
	ImageIcon[] arabalar;
	ImageIcon[] kullanilacakIconlar;
	static ImageIcon[] siralama;
	JLabel[] sonucTablosu;	
	JLabel[] basliklar;
	JButton[] destekleButonlari;
	ImageIcon desteklenenAraba;
	int k=0;
	
	public Oyun()
	{
		super("Araba Yarışı");		
		int en = 1000, boy = 800;
		this.setSize(en, boy);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);		
		
		arabalar = new ImageIcon[9];
		kullanilacakIconlar = new ImageIcon[9];
		for(int i=0 ; i<arabalar.length ; i++) {
			arabalar[i] = new ImageIcon("ProjeResimleri/"+(i+1)+".png");
			Image image = arabalar[i].getImage();
			Image image2 = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
			kullanilacakIconlar[i] = new ImageIcon(image2);
		}

		paneller = new JPanel[4];
		for(int i=0 ; i<paneller.length ; i++) {
			paneller[i] = new JPanel();			
			if(i==0) {
				paneller[i].setBounds(0, 0, en, 60);
				paneller[i].setLayout(new FlowLayout());
				paneller[i].setBackground(Color.BLACK);				
			}else if(i==1){
				paneller[i].setBounds(0, 60, en, 700);
				paneller[i].setLayout(new GridLayout(9, 8));
				paneller[i].setBackground(Color.GREEN);				
			}else if(i==2){ 
				paneller[i].setSize(en, boy);
				paneller[i].setLayout(new GridLayout(12, 0));
				paneller[i].setBackground(Color.GREEN);
			}else {    // paneller[3]:
				paneller[i].setSize(en, boy);
				paneller[i].setLayout(new FlowLayout());
				paneller[i].setBackground(Color.GREEN);
				this.add(paneller[i]);
			}
		}
		
		// paneller[3]'ün hazırlanması:
		basliklar = new JLabel[2];
		destekleButonlari = new JButton[9];
		
		basliklar[0] = new JLabel("<html>BAŞLAMADAN ÖNCE AŞAĞIDAKİ ARABALARDAN BİRİNİ YARIŞTA DESTEKLEYİN<br><br><br></html>");
		basliklar[0].setForeground(Color.BLACK);
		basliklar[0].setFont(new Font("Verdana", Font.BOLD, 20));
		basliklar[0].setHorizontalAlignment(0);
		paneller[3].add(basliklar[0]);
		
		for(int i=0 ; i<destekleButonlari.length ; i++) {
			destekleButonlari[i] = new JButton(kullanilacakIconlar[i]);
			destekleButonlari[i].setPreferredSize(new Dimension(400, 100));
			destekleButonlari[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
			destekleButonlari[i].setBackground(Color.LIGHT_GRAY);
			destekleButonlari[i].addActionListener(this);
			paneller[3].add(destekleButonlari[i]);
		}
		
		// paneller[0] ve paneller[1]'in hazırlanması:
		basliklar[1] = new JLabel("ARABA YARIŞI");
		basliklar[1].setForeground(Color.WHITE);
		basliklar[1].setFont(new Font("Verdana", Font.BOLD, 25));
		basliklar[1].setHorizontalAlignment(0);
		paneller[0].add(basliklar[1]);
		
		yol = new JButton[72];
		for(int i=0 ; i<yol.length ; i++) {
			yol[i] = new JButton();
			if(i%8==0) {				
				yol[i] = new JButton(kullanilacakIconlar[k]);	
				baslangicKonumu.add(i);
				k++;
			}else if((i-7)%8==0) 				
				bitisKonumu.add(i);		
			
			yol[i].setContentAreaFilled(false);
			yol[i].setBorderPainted(false);
			paneller[1].add(yol[i]);
		}				
		
		siralama = new ImageIcon[arabalar.length];		
		
		// Arabaların hız ve konumlarının belirlenip Araba sınıfına gönderilmesi
		arabaGorevleri = new Araba[9];
		for(int i=0 ; i<arabaGorevleri.length ; i++) {
			double hiz;
			hiz = Math.random()*2 + 0.3;
			arabaGorevleri[i] = new Araba(arabalar[i],hiz, baslangicKonumu.get(i), yol, bitisKonumu.get(i));
		}		
		
		this.setVisible(true);
		
		while(true) {
			synchronized (Araba.class) {
				if(Araba.bittiMi)
					break;
			}
		}
		
		// Araba sınıfından gelen sonuçlara göre sonuç tablosunun oluşturulması:
		sonucTablosu = new JLabel[12];		
		for(int j=0 ; j<sonucTablosu.length ; j++) {
			if(j==0)
				sonucTablosu[j] = new JLabel("SONUÇ TABLOSU");
			else if(j<10) {								
				sonucTablosu[j] = new JLabel();
				sonucTablosu[j].setIcon(siralama[j-1]);
				sonucTablosu[j].setText(" resimli araba yarışı " + j + ". sırada bitirdi.");
			}else if(j==10){				
				if(compareImages(desteklenenAraba.getImage(), siralama[0].getImage()))
					sonucTablosu[j] = new JLabel("  resimli desteklediğiniz araba yarışı kazandı :)");
				else
					sonucTablosu[j] = new JLabel("  resimli desteklediğiniz araba, maalesef yarışı kazanamadı :(");
				sonucTablosu[j].setIcon(desteklenenAraba);
			}else
				sonucTablosu[j] = new JLabel();
			sonucTablosu[j].setForeground(Color.BLACK);
			sonucTablosu[j].setFont(new Font("Verdana", Font.BOLD, 22));
			sonucTablosu[j].setHorizontalAlignment(0);
			paneller[2].add(sonucTablosu[j]);
		}
		
		paneller[0].setVisible(false);
		paneller[1].setVisible(false);
		this.add(paneller[2]);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		JButton basilanButon = (JButton)e.getSource();
		
		for (int i=0 ; i<destekleButonlari.length ; i++) {
			if(basilanButon == destekleButonlari[i]) {
				desteklenenAraba = kullanilacakIconlar[i];
				paneller[3].setVisible(false);
				this.add(paneller[0]);
				this.add(paneller[1]);
				for(int j=0 ; j<arabaGorevleri.length ; j++) {
					new Thread(arabaGorevleri[j]).start();
				}				
			}
		}		
	}
	
	// İkon'ların karşılaştırılması için gerekli metotlar:
	public static boolean compareImages(Image img1, Image img2) {
        BufferedImage bImg1 = toBufferedImage(img1);
        BufferedImage bImg2 = toBufferedImage(img2);

        if (bImg1.getWidth() != bImg2.getWidth() || bImg1.getHeight() != bImg2.getHeight()) {
            return false;
        }

        DataBuffer db1 = bImg1.getRaster().getDataBuffer();
        DataBuffer db2 = bImg2.getRaster().getDataBuffer();

        if (db1.getSize() != db2.getSize()) {
            return false;
        }

        for (int i = 0; i < db1.getSize(); i++) {
            if (db1.getElem(i) != db2.getElem(i)) {
                return false;
            }
        }

        return true;
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bImg;
    }
	
}
