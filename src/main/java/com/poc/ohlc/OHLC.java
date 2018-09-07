package com.poc.ohlc;

import java.util.Objects;

import javax.json.JsonArray;

import tmp.KrakenPublicRequest;

public class OHLC {

	/* exemple XETHZEUR */
	private final String pair;
	/* exemple 5 */
	private final int grain;
	private JsonArray dataFromKraken;

	public OHLC(String pair, int grain) {
		super();
		this.pair = Objects.requireNonNull(pair);
		this.grain = Objects.requireNonNull(grain);
	}

	public String getPair() {
		return pair;
	}
	public int getGrain() {
		return grain;
	}
	public JsonArray getDataFromKraken() {
		return dataFromKraken;
	}

	@Override
	public int hashCode() {
		return pair.hashCode() + grain;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof OHLC)) {
			return false;
		}
		OHLC other = (OHLC) obj;
		return (pair.equals(other.getPair())) && (grain == other.getGrain());
	}

	@Override
	public String toString() {
		return "OHLC [pair=" + pair + ", grain=" + grain + "]";
	}
	
	/** */
	public JsonArray refresh() {
		JsonArray dataFromKraken = new KrakenPublicRequest().queryPublic("fetchOHLCV", pair, grain);
		if(dataFromKraken != null) {
			/* cas timeout Kraken */
			this.dataFromKraken = dataFromKraken;
		}
		return this.dataFromKraken;
	}
}
