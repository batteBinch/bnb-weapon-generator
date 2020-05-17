package application

import weapon.Rarity
import weapon.Weapon

import javax.swing.*
import java.awt.*
import java.util.List

import static java.awt.GridBagConstraints.LINE_START

/**
 * The application which handles the display of the weapon generator.
 * @author Sasha Price
 */
class Application {
    private static Weapon source = new Weapon()

    private static final JFrame frame = new JFrame('B&B Weapon Generator')
    private static final JPanel panel = new JPanel(new GridBagLayout())
    private static final JComboBox level = [1..20 as int[]]
    private static final JComboBox rarity = [Rarity.values()]
    private static final JLabel name = new JLabel()
    private static final JLabel manufacturer = new JLabel()
    private static final JLabel damage = new JLabel()
    private static final JLabel clipSize = new JLabel()
    private static final JLabel range = new JLabel()
    private static final JLabel reloadSpeed = new JLabel()
    private static final JTextArea description = [lineWrap: true, wrapStyleWord: true,
                                                  opaque: false, editable: false]
    private static final JButton button = ['Generate Weapon']

    private static final List<String> fieldNames = ['Level', 'Rarity', 'Name', 'Manufacturer', 'Damage',
                                                    'Clip Size', 'Range', 'Reload Speed', 'Description']
    private static final List<JComponent> fields = [level, rarity, name, manufacturer, damage,
                                                    clipSize, range, reloadSpeed, description]

    static void main(String[] args) {
        initializeFields()
        initializePanel()
        initializeFrame()
    }

    private static void initializeFields() {
        level.addActionListener(event -> {
            JComboBox comboBox = event.source as JComboBox
            source.level = comboBox.selectedItem as int
            refresh()
        })

        rarity.addActionListener(event -> {
            JComboBox comboBox = event.source as JComboBox
            source.rarity = comboBox.selectedItem as Rarity
            refresh()
        })

        button.addActionListener(event -> {
            source = WeaponGenerator.generateRandomWeapon(level.selectedItem as int,
                                                          rarity.selectedItem as Rarity)
            refresh()
        })
    }

    private static void initializePanel() {
        for (i in 0..<fieldNames.size()) {
            panel.add(new JLabel("${fieldNames[i]}:"),
                      new GridBagConstraints(gridy: i, anchor: LINE_START, insets: [3, 10, 3, 10]))
        }

        for (i in 0..<fields.size()) {
            panel.add(fields[i], new GridBagConstraints(gridx: 1, gridy: i, insets: [3, 10, 3, 10]))
        }

        panel.add(button, new GridBagConstraints(gridy: 9, gridwidth: 2, insets: [15, 10, 10, 10]))
    }

    private static void initializeFrame() {
        frame.add(panel)
        frame.size = [325, 400]
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.visible = true
    }

    private static void refresh() {
        name.text = source.name ?: ''
        manufacturer.text = source.manufacturer ?: ''
        damage.text = source.damage ?: ''
        clipSize.text = source.clipSize ?: ''
        range.text = source.range ?: ''
        reloadSpeed.text = source.reloadSpeed ?: ''

        List<String> sourceDescription = source.description
        description.text = sourceDescription
                ? buildDescription(sourceDescription)
                : ''
    }

    private static String buildDescription(List<String> input) {
        StringBuilder builder = new StringBuilder('<html>')
        for (int i = 0, j = 0; i < input.size(); ++i) {
            String line = input[i]
            if (line.startsWith('<strong>') && line.endsWith('</strong>')
                    || line.startsWith('<b>') && line.endsWith('</b>')) {
                j = i
            }
            else if (i > j + 1) {
                builder.append('\t')
            }
            builder.append(line)
            if (i != input.size() - 1) {
                builder.append(System.lineSeparator())
            }
        }
        builder.append('</html>').toString()
    }
}