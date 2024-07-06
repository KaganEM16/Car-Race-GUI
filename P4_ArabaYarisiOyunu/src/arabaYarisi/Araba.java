package arabaYarisi;

import java.awt.Image;
import javax.swing.*;

public class Araba implements Runnable
{
	static int bitisSayisi=0;
	static boolean bittiMi;
	private ImageIcon araba;
	private double hiz;
	private int baslangicKonumu;
	private JButton[] yol;
	private int bitisNoktasi;	
		
	public Araba(ImageIcon araba, double hiz, int baslangicKonumu, JButton[] yol, int bitisNoktasi) 
	{		
		this.araba = araba;
		this.hiz = hiz;
		this.baslangicKonumu = baslangicKonumu;	
		this.yol = yol;
		this.bitisNoktasi = bitisNoktasi;
	}	

	@Override
	public void run() 
	{
		while(true) {
			try {
				Thread.sleep((int)(hiz*1000));				
				Image arabaninResmi = araba.getImage();
				Image arabaninAyarlanmisResmi = arabaninResmi.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
				ImageIcon kullanilacakResim = new ImageIcon(arabaninAyarlanmisResmi);				
				yol[baslangicKonumu].setIcon(null);
				yol[baslangicKonumu+1].setIcon(kullanilacakResim);				
				baslangicKonumu++;
				if(baslangicKonumu == bitisNoktasi) {								
					Oyun.siralama[bitisSayisi] = new ImageIcon(arabaninAyarlanmisResmi);					
					bitisSayisi++;
					if(bitisSayisi == 9)
						bittiMi = true;					
					break;
				}
				hiz = Math.random()*2 + 0.5;				
			}catch(Exception ex) {}
		}		
	}
}
