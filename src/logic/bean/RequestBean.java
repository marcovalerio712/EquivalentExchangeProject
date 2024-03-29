package logic.bean;

public class RequestBean {
	
	private String buyer;
	private ItemInSaleBean referredItemBean;
	private Boolean requestStatus;
	private String note;
	
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public Integer getReferredItem() {
		return referredItemBean.getItemID();
	}
	public void setReferredItem(Integer itemID) {
		this.referredItemBean.setItemID(itemID);
	}
	public Boolean getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(Boolean requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public ItemInSaleBean getReferredItemBean() {
		return referredItemBean;
	}
	public void setReferredItemBean(ItemInSaleBean referredItemBean) {
		this.referredItemBean = referredItemBean;
	}
	
}
