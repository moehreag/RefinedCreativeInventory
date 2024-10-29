package io.github.darkkronicle.refinedcreativeinventory.search;

import java.util.ArrayList;
import java.util.List;

import io.github.darkkronicle.konstruct.functions.Function;
import io.github.darkkronicle.konstruct.functions.NamedFunction;
import io.github.darkkronicle.konstruct.functions.Variable;
import io.github.darkkronicle.konstruct.nodes.Node;
import io.github.darkkronicle.konstruct.parser.IntRange;
import io.github.darkkronicle.konstruct.parser.NodeProcessor;
import io.github.darkkronicle.konstruct.parser.ParseContext;
import io.github.darkkronicle.konstruct.parser.Result;
import io.github.darkkronicle.konstruct.reader.builder.NodeBuilder;
import io.github.darkkronicle.konstruct.type.BooleanObject;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;

public class KonstructSearch implements ItemSearch {

    private final Node node;
    private final NodeProcessor processor;

    public KonstructSearch(Node node) {
        this.node = node;
        processor = new NodeProcessor();
        processor.addFunction(new NamedFunction() {
            @Override
            public String getName() {
                return "not";
            }

            @Override
            public Result parse(ParseContext context, List<Node> input) {
                Result result = Function.parseArgument(context, input, 0);
                if (Function.shouldReturn(result)) return result;
                return Result.success(new BooleanObject(!result.getContent().getBoolean()));
            }

            @Override
            public IntRange getArgumentCount() {
                return IntRange.of(1);
            }
        });
    }

    @Override
    public List<InventoryItem> search(List<InventoryItem> items) {
        try {
            return items.stream().filter(item -> {
                ParseContext context = processor.createContext();
                context.addLocalVariable("item", Variable.of(new InventoryItemObject(item)));
                Result result = node.parse(context);
                return result.getContent().getBoolean();
            }).toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static KonstructSearch fromString(String string) {
        try {
            return new KonstructSearch(new NodeBuilder(string).build());
        } catch (Exception exception) {
            return null;
        }
    }

}
