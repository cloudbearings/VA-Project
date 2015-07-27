package com.arthurbergmz.assistant.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SafeList<T> {
	
	private List<T> l;
	
	public SafeList(int capacity){
		this.l = new ArrayList<T>();
	}
	
	public SafeList(){
		this.l = new ArrayList<T>();
	}
	
	public SafeList(Collection<? extends T> collection){
		this.l = new ArrayList<T>(collection);
	}
	
	public boolean add(T object){
		if(!this.l.contains(object)) return this.l.add(object);
		return false;
	}
	
	public void addAll(Collection<? extends T> object){
		this.l.addAll(object);
	}
	
	
	public boolean remove(T object){
		if(this.l.contains(object)) return this.l.remove(object);
		return false;
	}
	
	
	public void remove(Collection<? extends T> object){
		for(T o : object) if(this.l.contains(o)) this.l.remove(o);
	}
	
	
	public boolean contains(T object){
		return this.l.contains(object);
	}
	
	
	public boolean contains(Collection<? extends T> object){
		return this.l.containsAll(object);
	}
	
	
	public T get(int i){
		return this.l.get(i);
	}
	
	
	public int size(){
		return this.l.size();
	}
	
	
	public boolean isEmpty(){
		return this.l.isEmpty();
	}
	
	public ArrayList<T> getRawList(){
		return (ArrayList<T>) this.l;
	}
	
}
