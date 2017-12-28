package me.missionary.blueberry.utils.inventory.content;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.utils.inventory.ClickableItem;
import me.missionary.blueberry.utils.inventory.InventoryManager;
import me.missionary.blueberry.utils.inventory.SmartInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public interface InventoryContents {

    SmartInventory inventory();

    Pagination pagination();

    Optional<SlotIterator> iterator(String id);

    SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn);

    SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn);

    ClickableItem[][] all();

    Optional<ClickableItem> get(int row, int column);

    InventoryContents set(int row, int column, ClickableItem item);

    InventoryContents add(ClickableItem item);

    InventoryContents fill(ClickableItem item);

    InventoryContents fillRow(int row, ClickableItem item);

    InventoryContents fillColumn(int column, ClickableItem item);

    InventoryContents fillBorders(ClickableItem item);

    InventoryContents fillRect(int fromRow, int fromColumn,
                               int toRow, int toColumn, ClickableItem item);

    <T> T property(String name);

    <T> T property(String name, T def);

    InventoryContents setProperty(String name, Object value);

    class Impl implements InventoryContents {

        private SmartInventory inv;
        private ClickableItem[][] contents;

        private Pagination pagination = new Pagination.Impl();
        private Map<String, SlotIterator> iterators = new HashMap<>();
        private Map<String, Object> properties = new HashMap<>();

        public Impl(SmartInventory inv) {
            this.inv = inv;
            this.contents = new ClickableItem[inv.getRows()][inv.getColumns()];
        }

        @Override
        public SmartInventory inventory() {
            return inv;
        }

        @Override
        public Pagination pagination() {
            return pagination;
        }

        @Override
        public Optional<SlotIterator> iterator(String id) {
            return Optional.ofNullable(this.iterators.get(id));
        }

        @Override
        public SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn) {
            SlotIterator iterator = new SlotIterator.Impl(this, inv,
                    type, startRow, startColumn);

            this.iterators.put(id, iterator);
            return iterator;
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn) {
            return new SlotIterator.Impl(this, inv, type, startRow, startColumn);
        }

        @Override
        public ClickableItem[][] all() {
            return contents;
        }

        @Override
        public Optional<ClickableItem> get(int row, int column) {
            if (row >= contents.length || column >= contents[row].length) {
                return Optional.empty();
            }
            return Optional.ofNullable(contents[row][column]);
        }

        @Override
        public InventoryContents set(int row, int column, ClickableItem item) {
            if (row >= contents.length || column >= contents[row].length) {
                return this;
            }
            contents[row][column] = item;
            update(row, column, item != null ? item.getItem() : null);
            return this;
        }

        @Override
        public InventoryContents fill(ClickableItem item) {
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[row].length; column++) {
                    set(row, column, item);
                }
            }
            return this;
        }

        @Override
        public InventoryContents fillRow(int row, ClickableItem item) {
            if (row >= contents.length)
                return this;

            IntStream.range(0, contents[row].length).forEach(column -> set(row, column, item));
            return this;
        }

        @Override
        public InventoryContents fillColumn(int column, ClickableItem item) {
            IntStream.range(0, contents.length).forEach(row -> set(row, column, item));
            return this;
        }

        @Override
        public InventoryContents fillBorders(ClickableItem item) {
            fillRect(0, 0, inv.getRows() - 1, inv.getColumns() - 1, item);
            return this;
        }

        @Override
        public InventoryContents add(ClickableItem item) {
            for (int i = inv.getRows(); i < inv.getRows(); i++){
                for (int j = inv.getColumns(); j < inv.getColumns(); j++){
                    if (contents[i][j] == null){
                        set(i, j, item);
                    }
                }
            }
            return this;
        }

        @Override
        public InventoryContents fillRect(int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item) {
            for (int row = fromRow; row <= toRow; row++) {
                for (int column = fromColumn; column <= toColumn; column++) {
                    if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) {
                        continue;
                    }
                    set(row, column, item);
                }
            }

            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T property(String name) {
            return (T) properties.get(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T property(String name, T def) {
            return properties.containsKey(name) ? (T) properties.get(name) : def;
        }

        @Override
        public InventoryContents setProperty(String name, Object value) {
            properties.put(name, value);
            return this;
        }

        private void update(int row, int column, ItemStack item) {
            InventoryManager manager = Blueberry.getPlugin().getInventoryManager();
            manager.getOpenedPlayers(inv).forEach(p -> p.getOpenInventory().getTopInventory().setItem(9 * row + column, item));
        }

    }

}