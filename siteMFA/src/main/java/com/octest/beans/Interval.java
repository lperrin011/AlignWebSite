package com.octest.beans;

public class Interval {
		private String text;
		private float xmin;
		private float xmax;
		
		public Interval(String text, float xmin, float xmax) {
			this.text = text;
			this.xmin = xmin;
			this.xmax = xmax;
		}

		public String getText() {
			return text;
		}

		public float getXmin() {
			return xmin;
		}

		public float getXmax() {
			return xmax;
		}	

}
