package com.fral.spring.billing.utils.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fral.spring.billing.utils.paginator.PageItem;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	
	private int totalPages;
	
	private int itemsPerPage;
	
	private int currentPage;
	
	private List<PageItem> pages;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();
		
		itemsPerPage = page.getSize();
		totalPages = page.getTotalPages();
		currentPage = page.getNumber() + 1;
		
		int desde, hasta;
		if(totalPages <= itemsPerPage) {
			desde = 1;
			hasta = totalPages;
		} else {
			if(currentPage <= itemsPerPage/2) {
				desde = 1;
				hasta = itemsPerPage;
			} else if(currentPage >= totalPages - itemsPerPage/2 ) {
				desde = totalPages - itemsPerPage + 1;
				hasta = itemsPerPage;
			} else {
				desde = currentPage -itemsPerPage/2;
				hasta = itemsPerPage;
			}
		}
		
		for(int i=0; i < hasta; i++) {
			pages.add(new PageItem(desde + i, currentPage == desde+i));
		}

	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getPaginaActual() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
