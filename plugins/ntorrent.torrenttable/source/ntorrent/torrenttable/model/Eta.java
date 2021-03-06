/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ntorrent.torrenttable.model;

import java.util.Comparator;


public class Eta implements Comparable<Eta>, Comparator<Eta>{

	private Integer sec;
	private Integer[] div = {60,60,24,24,7,4,12};
	private String[] unit = {"s","m","h","D","W","M","Y"};
	private int l = 0;
	
	public Eta(Integer sec) {
		this.sec = sec;
	}
		
	@Override
	public String toString() {
		int x = sec;
		int rest = 0;
		while(!(x < div[l])){
			rest = x%div[l];
			x /= div[l++];
		}

			if(x == 0)
				return "-";
			else{
				String out = x+unit[l];
				if(l > 0)
					out += " "+rest+unit[l-1];
				return out;
			}
	}

	public int compareTo(Eta o) {
		return sec.compareTo(o.sec);
	}

	public int compare(Eta o1, Eta o2) {
		return o1.compareTo(o2);
	}

}
