package model;

public class ImgCoordenate {
	private String imgPath;
	private double latitude;
	private double longitude;

	public ImgCoordenate(String imgPath, double latitude, double longitude) {
		super();
		this.imgPath = imgPath;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	

}
