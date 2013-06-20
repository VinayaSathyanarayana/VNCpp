/*
 	Copyright 2013 Oscar Crespo Salazar
 	Copyright 2013 Gorka Jimeno Garrachon
 	Copyright 2013 Luis Valero Martin
  
	This file is part of VNCpp.

	VNCpp is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	any later version.
	
	VNCpp is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with VNCpp.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.farfuteam.vncpp.view;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import es.farfuteam.vncpp.controller.ActivityTabs;
import es.farfuteam.vncpp.controller.EditionActivity;
import es.farfuteam.vncpp.controller.R;
import es.farfuteam.vncpp.model.sql.Connection;
import es.farfuteam.vncpp.model.sql.ConnectionSQLite;
import es.farfuteam.vncpp.view.DialogOptions.SuperListener;

/**
 * @class ListFragmentTabFav
 * @brief This is class which controls the favorites list
 * 
 * This is the detailed description
 *
 * @authors Oscar Crespo, Gorka Jimeno, Luis Valero
 * @extends ListFragment
 * @implements SuperListener
 */
public class ListFragmentTabFav extends ListFragment implements SuperListener{
	
	//variable donde se guarda el Objeto User al pulsarlo en la lista
	private static Object o;
	
	private static Adapter adapter;
	private ArrayList<Connection> userList;
	private static ConnectionSQLite admin;
	private View view;
	
	/**
	 * @brief Constructor of ListFragmentTabFav
	 * @details Constructor of ListFragmentTabFav
	 */   
	public ListFragmentTabFav() {

	}

	/**
	 * @brief Method that create the list view
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @details Method that create the list view
	 */ 
	@Override
	  public View onCreateView(LayoutInflater inflater, 
	                 ViewGroup container, Bundle savedInstanceState) {
		
		setUserList(new ArrayList<Connection>());
	    // Inflate the layout for this fragment
	    view = inflater.inflate(R.layout.list_users, container, false);

	    // Se vincula Adaptador
        admin = new ConnectionSQLite(this.getActivity());
        admin.getWritableDatabase();
        adapter = new Adapter(this.getActivity(),admin.getAllFavUsers());
        setListAdapter(adapter);
	   
	    
	    return view;
	  }
	
	/**
	 * @brief Override method called on activity start
	 * @details Override method called on activity start
	 */ 
	@Override
	public void onStart() {
		
	    super.onStart();       	    
	    admin = ConnectionSQLite.getInstance(getActivity());
	    userList = admin.getAllFavUsers();
    	adapter = new Adapter(this.getActivity(),admin.getAllFavUsers());
        adapter.setList(admin.getAllFavUsers());
        setListAdapter(adapter);        	
      
	    
	}
	    
		/**
		 * @brief Method called when the user click on the list
		 * @param listView
		 * @param view
		 * @param position
		 * @param id
		 * @details Method called when the user click on the list and show the options dialog
		 */ 
	    @Override
		public void onListItemClick(ListView listView, View view, int position, long id) {	    	

			super.onListItemClick(listView, view, position, id);			
			setO(getListAdapter().getItem(position));			
			
			((ActivityTabs)getActivity()).setO(getO());
			
			//SuperListener parentFragment;
			DialogOptions dialog1 = DialogOptions.newInstance((SuperListener) this);
			dialog1.show(getFragmentManager(),"dialog");
		
		}
	    
		/**
		 * @brief Delete connection on the list
		 * @details Delete connection on the list
		 */
		@Override
		public void deleteUser() {
			
	        admin.getWritableDatabase();
	        admin.deleteUser((Connection) getO());
	        adapter.setList(admin.getAllFavUsers());
	        setListAdapter(adapter);
			
		}
		
		/**
		 * @brief Edit connection on the list
		 * @details Edit connection on the list
		 */
		@Override
		public void editingUser() {
			
			ConnectionSQLite admin = new ConnectionSQLite(this.getActivity());
	        admin.getWritableDatabase();
		
	        Intent intent = new Intent (this.getActivity(),EditionActivity.class);
	        
	        intent.putExtra("Name", ((Connection) getO()).getName());
	        intent.putExtra("IP", ((Connection) getO()).getIP());
	        intent.putExtra("PORT", ((Connection) getO()).getPORT());
	        intent.putExtra("PSW", ((Connection) getO()).getPsw());

	        startActivity(intent);
	        
		}

		/**
		 * @brief Returns the Object clicked on the Favlist
		 * @return o
		 * @details Returns the Object clicked on the Favlist
		 */
		public static Object getO() {
			return o;
		}
		
		/**
		 * @brief Sets the Object clicked on the Favlist
		 * @param o 
		 * @details Sets the Object clicked on the Favlist
		 */
		public static void setO(Object o) {
			ListFragmentTabFav.o = o;
		}

		/**
		 * @brief Returns the userList attribute
		 * @return userList
		 * @details Returns the userList attribute
		 */
		public ArrayList<Connection> getUserList() {
			return userList;
		}

		/**
		 * @brief Sets the userList attribute
		 * @param userList 
		 * @details Sets the userList attribute
		 */
		public void setUserList(ArrayList<Connection> userList) {
			this.userList = userList;
		}	    
	  	  

}
