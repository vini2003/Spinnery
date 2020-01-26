package spinnery.widget;

public interface WModifiableCollection extends WCollection {
	void add(WWidget... widgets);

	void remove(WWidget... widgets);
}
