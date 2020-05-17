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

    private static final JFrame FRAME = new JFrame('B&B Weapon Generator')
    private static final JPanel PANEL = new JPanel(new GridBagLayout())
    private static final JComboBox LEVEL = [1..20 as int[]]
    private static final JComboBox RARITY = [Rarity.values()]
    private static final JLabel NAME = new JLabel()
    private static final JLabel MANUFACTURER = new JLabel()
    private static final JLabel DAMAGE = new JLabel()
    private static final JLabel CLIP_SIZE = new JLabel()
    private static final JLabel RANGE = new JLabel()
    private static final JLabel RELOAD_SPEED = new JLabel()
    private static final JTextArea DESCRIPTION = [lineWrap: true, wrapStyleWord: true,
                                                  opaque: false, editable: false]
    private static final JButton button = ['Generate Weapon']
    private static final Dimension DEFAULT_DIMENSIONS = [325, 400]

    private static final List<String> fieldNames = ['Level', 'Rarity', 'Name', 'Manufacturer', 'Damage',
                                                    'Clip Size', 'Range', 'Reload Speed', 'Description']
    private static final List<JComponent> fields = [LEVEL, RARITY, NAME, MANUFACTURER, DAMAGE,
                                                    CLIP_SIZE, RANGE, RELOAD_SPEED, DESCRIPTION]

    static void main(String[] args) {
        initializeFields()
        initializePanel()
        initializeFrame()
    }

    private static void initializeFields() {
        LEVEL.addActionListener(event -> {
            JComboBox comboBox = event.source as JComboBox
            source.level = comboBox.selectedItem as int
            refresh()
        })

        RARITY.addActionListener(event -> {
            JComboBox comboBox = event.source as JComboBox
            source.rarity = comboBox.selectedItem as Rarity
            refresh()
        })

        button.addActionListener(event -> {
            source = Generator.generateRandomWeapon(LEVEL.selectedItem as int,
                                                    RARITY.selectedItem as Rarity)
            refresh()
        })
    }

    private static void initializePanel() {
        for (i in 0..<fieldNames.size()) {
            PANEL.add(new JLabel("${fieldNames[i]}:"),
                      new GridBagConstraints(gridy: i, anchor: LINE_START, insets: [3, 10, 3, 10]))
        }

        for (i in 0..<fields.size()) {
            PANEL.add(fields[i], new GridBagConstraints(gridx: 1, gridy: i, insets: [3, 10, 3, 10]))
        }

        PANEL.add(button, new GridBagConstraints(gridy: 9, gridwidth: 2, insets: [15, 10, 10, 10]))
    }

    private static void initializeFrame() {
        FRAME.add(PANEL)
        FRAME.size = DEFAULT_DIMENSIONS
        FRAME.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        FRAME.visible = true
    }

    private static void refresh() {
        NAME.text = source.name ?: ''
        MANUFACTURER.text = source.manufacturer ?: ''
        DAMAGE.text = source.damage ?: ''
        CLIP_SIZE.text = source.clipSize ?: ''
        RANGE.text = source.range ?: ''
        RELOAD_SPEED.text = source.reloadSpeed ?: ''

        List<String> sourceDescription = source.description
        DESCRIPTION.text = sourceDescription
                ? buildDescription(sourceDescription)
                : ''
    }

    private static String buildDescription(List<String> input) {
        StringBuilder builder = new StringBuilder('<html>')
        for (int i = 0, j = 0; i < input.size(); ++i) {
            String line = input[i]
            if (line.startsWith('<strong>') && line.endsWith('</strong>')) {
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