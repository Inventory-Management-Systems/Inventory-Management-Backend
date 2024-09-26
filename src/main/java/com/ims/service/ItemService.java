package com.ims.service;

import java.util.List;

import com.ims.Model.Item;

public interface ItemService {
	public List<Item> getAllItems();

	public int getItemCount();
	public boolean addItem(Item item);

	public boolean deleteItem(Item item);

	public boolean updateItem(Item item, int id);


}
